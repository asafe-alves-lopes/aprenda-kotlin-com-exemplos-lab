import kotlin.math.ceil

enum class Level(val levelName: String, val weight: Int) {
    BASIC("Básico", 1),
    INTERMEDIARY("Intermediário", 2),
    ADVANCED("Avançado", 3),
    SPECIALIST("Especialista", 4)
}

enum class ActivityType(val typeName: String) {
    COURSE("Curso"),
    CODE_CHALLENGE("Desafio de Código"),
    PROJECT_CHALLENGE("Desafio de Projeto")
}

data class User(val userName: String)

data class Activity(
    val activityName: String,
    val duration: Int,
    val level: Level,
    val type: ActivityType = ActivityType.COURSE
)

class EducationalContent(val contentName: String, val activities: List<Activity>) {
    fun durationOfActivities() = activities.sumOf { it.duration }
    fun numberOfActivities() = activities.size
    fun numberOfActivitiesByType(activityType: ActivityType) =
        activities.filter { activity -> activity.type == activityType }.size

    fun highestLevelOfAnActivity() = activities.maxBy { activity -> activity.level.weight }.level
}

class Training(
    val trainingName: String,
    val contents: List<EducationalContent>,
    val description: String
) {
    private val enrolled = mutableListOf<User>()

    fun enroll(vararg user: User) {
        user.forEach {
            enrolled.add(it)
        }
    }

    fun numberOfEnrolled() = enrolled.size
    fun trainingDuration() = contents.sumOf { content -> content.durationOfActivities() }
    fun numberOfTrainingActivitiesByType(activityType: ActivityType) =
        contents.sumOf { content -> content.numberOfActivitiesByType(activityType) }

    fun trainingLevel(): Level {
        val numberOfTrainingActivities = contents.sumOf { it.numberOfActivities() }
        val sumOfWeightOfTrainingActivities =
            contents.sumOf { content -> content.activities.sumOf { activity -> activity.level.weight } }
        val trainingLevel =
            Level.entries.find { it.weight == ceil(sumOfWeightOfTrainingActivities / numberOfTrainingActivities.toFloat()).toInt() }
        return trainingLevel ?: contents.maxBy { content -> content.highestLevelOfAnActivity().weight }
            .highestLevelOfAnActivity()
    }

    fun summaryOfTraining(): String {
        val sb = StringBuilder()

        sb.append("${trainingName}\n")
        sb.append("Nível da formação: ${trainingLevel().levelName} - ")
        sb.append("${fromMinutesToHours(trainingDuration())} hrs - ")
        sb.append("${numberOfEnrolled()} pessoas já se matricularam\n\n")
        sb.append("${description}\n\n")
        sb.append("${numberOfTrainingActivitiesByType(ActivityType.COURSE)} cursos - ")
        sb.append("${numberOfTrainingActivitiesByType(ActivityType.PROJECT_CHALLENGE)} desafios de projeto - ")
        sb.append("${numberOfTrainingActivitiesByType(ActivityType.CODE_CHALLENGE)} desafio de código\n\n\n")

        contents.forEach { content ->
            sb.append("\t${content.contentName} ${content.numberOfActivities()} Atividades\n\n")

            content.activities.forEach { activity ->
                sb.append("\t\t${activity.type.typeName}: ")
                sb.append("${activity.activityName}\n")
                sb.append("\t\tNível: ${activity.level.levelName}\n")
                sb.append("\t\tDuração: ${fromMinutesToHours(activity.duration)} hrs")
                sb.append("\n\n")
            }

            sb.append("\n")
        }

        return sb.toString()
    }
}

fun fromMinutesToHours(minutes: Int) = ceil(minutes / 60.0).toInt()

fun main() {
    val basicKotlin = listOf(
        Activity(activityName = "Kotlin I", duration = 60, Level.BASIC),
        Activity(activityName = "Kotlin II", duration = 120, Level.BASIC),
        Activity(activityName = "Kotlin III", duration = 180, Level.BASIC)
    )
    val intermediaryKotlin = listOf(
        Activity(activityName = "Kotlin I", duration = 60, Level.INTERMEDIARY),
        Activity(activityName = "Kotlin II", duration = 120, Level.INTERMEDIARY),
        Activity(activityName = "Kotlin III", duration = 180, Level.INTERMEDIARY),
        Activity(
            activityName = "Kotlin Desafio de Código",
            duration = 240,
            Level.ADVANCED,
            type = ActivityType.CODE_CHALLENGE
        ),
        Activity(
            activityName = "Kotlin Desafio de Projeto",
            duration = 300,
            Level.ADVANCED,
            type = ActivityType.PROJECT_CHALLENGE
        )
    )
    val advancedKotlin = listOf(
        Activity(activityName = "Kotlin I", duration = 60, Level.ADVANCED),
        Activity(activityName = "Kotlin II", duration = 120, Level.ADVANCED),
        Activity(activityName = "Kotlin III", duration = 180, Level.ADVANCED),
        Activity(
            activityName = "Kotlin Desafio de Código",
            duration = 240,
            Level.SPECIALIST,
            type = ActivityType.CODE_CHALLENGE
        ),
        Activity(
            activityName = "Kotlin Desafio de Projeto",
            duration = 300,
            Level.SPECIALIST,
            type = ActivityType.PROJECT_CHALLENGE
        )
    )

    val kotlinTrainingContents = listOf(
        EducationalContent(contentName = "Kotlin Básico", activities = basicKotlin),
        EducationalContent(contentName = "Kotlin Intermediário", activities = intermediaryKotlin),
        EducationalContent(contentName = "Kotlin Avançado", activities = advancedKotlin)
    )

    val kotlinTraining = Training(
        trainingName = "Formação Kotlin Developer",
        contents = kotlinTrainingContents,
        description = "Formação para evoluir seus conhecimentos na linguagem de programação Kotlin"
    )

    val listOfUsersToEnroll = arrayOf(User("Asafe"), User("Alves"), User("Lopes"))
    kotlinTraining.enroll(*listOfUsersToEnroll)

    println(kotlinTraining.summaryOfTraining())
}