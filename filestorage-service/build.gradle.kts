import org.flywaydb.core.Flyway
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Database
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Generate
import org.jooq.meta.jaxb.GeneratedAnnotationType
import org.jooq.meta.jaxb.Generator
import org.jooq.meta.jaxb.Jdbc
import org.jooq.meta.jaxb.Strategy
import org.jooq.meta.jaxb.Target


plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    jacoco
}

group = "com.skfl"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

buildscript {
    val versionJooq: String by project
    val versionPostgresql: String by project
    val versionFlyway: String by project

    dependencies {
        classpath("org.jooq:jooq-codegen:$versionJooq")
        classpath("org.postgresql:postgresql:$versionPostgresql")
        classpath("org.flywaydb:flyway-core:$versionFlyway")
        classpath(files("src/main/resources"))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq-jackson-extensions:3.19.7")
    implementation("org.jooq:jooq-postgres-extensions:3.19.9")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val generatedSourcesDir = "${layout.buildDirectory.get()}/generated"
val jooqBuildDir = "$generatedSourcesDir/jooq/src/main/java"

sourceSets {
    main {
        java.srcDir(jooqBuildDir)
    }
}

tasks.register("jooqGenerate") {
    group = "build"

    doLast {
        val driverClassName = "org.postgresql.Driver"
        val filesJdbcUrl = "jdbc:postgresql://127.0.0.1:55321/filestorage"
        val filesUsername = "filestorage"
        val filesPassword = "filestorage"
        val filesIncludes = listOf("files")
        runMigration(filesJdbcUrl, filesUsername, filesPassword)
        runJooq(driverClassName, filesJdbcUrl, filesUsername, filesPassword, "files", filesIncludes)
    }
}

fun runMigration(jdbcUrl: String, username: String, password: String) {
    Flyway.configure()
        .dataSource(jdbcUrl, username, password)
        .load()
        .migrate()
}

fun runJooq(
    driverClassName: String,
    jdbcUrl: String,
    username: String,
    password: String,
    packagePostfix: String,
    includes: List<String>
) {
    GenerationTool.generate(
        Configuration()
            .withJdbc(
                Jdbc()
                    .withDriver(driverClassName)
                    .withUrl(jdbcUrl)
                    .withUser(username)
                    .withPassword(password)
            )
            .withGenerator(
                Generator()
                    .withDatabase(
                        Database()
                            .withInputSchema("public")
                            .withIncludes(includes.joinToString("|"))
                            .withForcedTypes(
                                ForcedType().apply {
                                    userType = "org.jooq.postgres.extensions.types.Hstore"
                                    binding = "org.jooq.postgres.extensions.bindings.HstoreBinding"
                                    includeTypes = "hstore"
                                    priority = Int.MIN_VALUE
                                }
                            )
                    )
                    .withStrategy(Strategy().apply { name = "org.jooq.codegen.example.JPrefixGeneratorStrategy" })
                    .withGenerate(
                        Generate()
                            .withPojos(true)
                            .withDaos(true)
                            .withGeneratedAnnotation(true)
                            .withGeneratedAnnotationType(GeneratedAnnotationType.JAVAX_ANNOTATION_PROCESSING_GENERATED)
                            .withPojosEqualsAndHashCode(true)
                    )
                    .withTarget(
                        Target()
                            .withPackageName("com.skfl.filestorageservice.jooq.$packagePostfix")
                            .withDirectory(jooqBuildDir)
                    )
            )
    )
}


kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

detekt {
    toolVersion = "1.23.6"
    config.setFrom(file("detekt.yml"))
    buildUponDefaultConfig = true
    ignoreFailures = true
    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
    }
}


tasks.withType<JacocoReport> {
    group = "verification"
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHTML"))
    }
}

tasks.withType<JacocoCoverageVerification> {
    group = "verification"
    dependsOn("jacocoTestReport")
    violationRules {
        rule {
            limit {
                minimum = "0.8".toBigDecimal()
            }
        }
    }
}