import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

tasks.withType<Test>().configureEach { useJUnit() }
extensions.configure<PublishingExtension> {
    publications.withType<MavenPublication>().configureEach {
        groupId = "industries.angelic.blur"
        pom {
            name.set("Blur Legacy")
            description.set("Configurable menu blur for Minecraft 1.7.10.")
            url.set("https://github.com/AngelicIndustries/BlurLegacy")
            licenses { license { name.set("MIT"); url.set("https://opensource.org/license/mit") } }
        }
    }
}
