rootProject.name = "icd01-team05_06-commerce-be"

include("commons")
include("commons:common-util")
include("commons:model")
include("commons:persistence-database")
include("commons:common-jwt")

include("commerce-admin")

include("commerce-service")
include("commerce-service:commerce-auth")
include("commerce-service:commerce-order")
