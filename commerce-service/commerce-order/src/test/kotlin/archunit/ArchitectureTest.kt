package archunit

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.library.Architectures.layeredArchitecture

const val DEFAULT_PACKAGE = "com.commerce.service.order"
const val PERSISTENCE_PACKAGE = "com.commerce.common.persistence"

@AnalyzeClasses(
    packages = [DEFAULT_PACKAGE, PERSISTENCE_PACKAGE],
    importOptions = [DoNotIncludeTests::class]
)
class ArchitectureTest {

    // TODO: 레이어 의존성 수정
    @ArchTest
    fun `레이어 의존성 테스트`(importedClasses: JavaClasses) {
        val rule = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy("${DEFAULT_PACKAGE}.controller..")
            .layer("Application").definedBy("${DEFAULT_PACKAGE}.application..")
            .layer("Persistence").definedBy("${PERSISTENCE_PACKAGE}..")

            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Persistence").mayNotBeAccessedByAnyLayer()
        rule.check(importedClasses)
    }

    @ArchTest
    fun `Config는 외부에서 접근할 수 없다`(importedClasses: JavaClasses) {
        val rule = classes()
            .that().resideInAPackage("${DEFAULT_PACKAGE}.config..")
            .should().onlyHaveDependentClassesThat().resideInAPackage("${DEFAULT_PACKAGE}.config..")
        rule.check(importedClasses)
    }
}