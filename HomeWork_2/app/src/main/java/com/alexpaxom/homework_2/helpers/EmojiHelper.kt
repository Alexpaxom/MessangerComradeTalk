package com.alexpaxom.homework_2.helpers

class EmojiHelper {
    fun getNameByUnicode(unicodeEmoji: String): String {
        return emojiMap[unicodeEmoji] ?: "grinning"
    }

    fun getUnicodeByName(emojiName: String): String? {
        return invertEmojiMap[emojiName]
    }

    // Собираем несколько символов в 1 эмоджи
    fun combineStringToEmoji(emojiString: String): String {
        return emojiString
            .split('-')
            .fold("") { emoji, elem ->
                emoji + hexToUnitcode(elem)
            }
    }

    fun hexToUnitcode(hex:String, radix:Int = 16): String {
        return String(Character.toChars(hex.toInt(radix)))
    }

    val emojiMap = mapOf(
        "1f600" to "grinning",
        "1f603" to "smiley",
        "1f604" to "big_smile",
        "1f601" to "grinning_face_with_smiling_eyes",
        "1f606" to "laughing",
        "1f605" to "sweat_smile",
        "1f602" to "joy",
        "1f923" to "rolling_on_the_floor_laughing",
        "263a" to "smiling_face",
        "1f60a" to "blush",
        "1f607" to "innocent",
        "1f642" to "smile",
        "1f643" to "upside_down",
        "1f609" to "wink",
        "1f60c" to "relieved",
        "1f60d" to "heart_eyes",
        "1f618" to "heart_kiss",
        "1f617" to "kiss",
        "1f619" to "kiss_smiling_eyes",
        "1f61a" to "kiss_with_blush",
        "1f60b" to "yum",
        "1f61b" to "stuck_out_tongue",
        "1f61c" to "stuck_out_tongue_wink",
        "1f61d" to "stuck_out_tongue_closed_eyes",
        "1f911" to "money_face",
        "1f917" to "hug",
        "1f913" to "nerd",
        "1f60e" to "sunglasses",
        "1f921" to "clown",
        "1f920" to "cowboy",
        "1f60f" to "smirk",
        "1f612" to "unamused",
        "1f61e" to "disappointed",
        "1f614" to "pensive",
        "1f61f" to "worried",
        "1f615" to "oh_no",
        "1f641" to "frown",
        "2639" to "sad",
        "1f623" to "persevere",
        "1f616" to "confounded",
        "1f62b" to "anguish",
        "1f629" to "weary",
        "1f624" to "triumph",
        "1f620" to "angry",
        "1f621" to "rage",
        "1f636" to "speechless",
        "1f610" to "neutral",
        "1f611" to "expressionless",
        "1f62f" to "hushed",
        "1f626" to "frowning",
        "1f627" to "anguished",
        "1f62e" to "open_mouth",
        "1f632" to "astonished",
        "1f635" to "dizzy",
        "1f633" to "flushed",
        "1f631" to "scream",
        "1f628" to "fear",
        "1f630" to "cold_sweat",
        "1f622" to "cry",
        "1f625" to "exhausted",
        "1f924" to "drooling",
        "1f62d" to "sob",
        "1f613" to "sweat",
        "1f62a" to "sleepy",
        "1f634" to "sleeping",
        "1f644" to "rolling_eyes",
        "1f914" to "thinking",
        "1f925" to "lying",
        "1f62c" to "grimacing",
        "1f910" to "silence",
        "1f922" to "nauseated",
        "1f927" to "sneezing",
        "1f637" to "mask",
        "1f912" to "sick",
        "1f915" to "hurt",
        "1f608" to "smiling_devil",
        "1f47f" to "devil",
        "1f479" to "ogre",
        "1f47a" to "goblin",
        "1f4a9" to "poop",
        "1f47b" to "ghost",
        "1f480" to "skull",
        "2620" to "skull_and_crossbones",
        "1f47d" to "alien",
        "1f47e" to "space_invader",
        "1f916" to "robot",
        "1f383" to "jack-o-lantern",
        "1f63a" to "smiley_cat",
        "1f638" to "smile_cat",
        "1f639" to "joy_cat",
        "1f63b" to "heart_eyes_cat",
        "1f63c" to "smirk_cat",
        "1f63d" to "kissing_cat",
        "1f640" to "scream_cat",
        "1f63f" to "crying_cat",
        "1f63e" to "angry_cat",
        "1f450" to "open_hands",
        "1f64c" to "raised_hands",
        "1f44f" to "clap",
        "1f64f" to "pray",
        "1f91d" to "handshake",
        "1f44d" to "+1",
        "1f44e" to "-1",
        "1f44a" to "fist_bump",
        "270a" to "fist",
        "1f91b" to "left_fist",
        "1f91c" to "right_fist",
        "1f91e" to "fingers_crossed",
        "270c" to "peace_sign",
        "1f918" to "rock_on",
        "1f44c" to "ok",
        "1f448" to "point_left",
        "1f449" to "point_right",
        "1f446" to "point_up",
        "1f447" to "point_down",
        "261d" to "wait_one_second",
        "270b" to "hand",
        "1f91a" to "stop",
        "1f590" to "high_five",
        "1f596" to "spock",
        "1f44b" to "wave",
        "1f919" to "call_me",
        "1f4aa" to "muscle",
        "1f595" to "middle_finger",
        "270d" to "writing",
        "1f933" to "selfie",
        "1f485" to "nail_polish",
        "1f48d" to "ring",
        "1f484" to "lipstick",
        "1f48b" to "lipstick_kiss",
        "1f444" to "lips",
        "1f445" to "tongue",
        "1f442" to "ear",
        "1f443" to "nose",
        "1f463" to "footprints",
        "1f441" to "eye",
        "1f440" to "eyes",
        "1f5e3" to "speaking_head",
        "1f464" to "silhouette",
        "1f465" to "silhouettes",
        "1f476" to "baby",
        "1f466" to "boy",
        "1f467" to "girl",
        "1f468" to "man",
        "1f469" to "woman",
        "1f474" to "older_man",
        "1f475" to "older_woman",
        "1f472" to "gua_pi_mao",
        "1f473" to "turban",
        "1f46e" to "police",
        "1f477" to "construction_worker",
        "1f482" to "guard",
        "1f575" to "detective",
        "1f936" to "mother_christmas",
        "1f385" to "santa",
        "1f478" to "princess",
        "1f934" to "prince",
        "1f470" to "bride",
        "1f935" to "tuxedo",
        "1f47c" to "angel",
        "1f930" to "pregnant",
        "1f647" to "bow",
        "1f481" to "information_desk_person",
        "1f645" to "no_signal",
        "1f646" to "ok_signal",
        "1f64b" to "raising_hand",
        "1f926" to "face_palm",
        "1f937" to "shrug",
        "1f64e" to "person_pouting",
        "1f64d" to "person_frowning",
        "1f487" to "haircut",
        "1f486" to "massage",
        "1f574" to "levitating",
        "1f483" to "dancer",
        "1f57a" to "dancing",
        "1f46f" to "dancers",
        "1f6b6" to "walking",
        "1f3c3" to "running",
        "1f46b" to "man_and_woman_holding_hands",
        "1f46d" to "two_women_holding_hands",
        "1f46c" to "two_men_holding_hands",
        "1f46a" to "family",
        "1f45a" to "clothing",
        "1f455" to "shirt",
        "1f456" to "jeans",
        "1f454" to "tie",
        "1f457" to "dress",
        "1f459" to "bikini",
        "1f458" to "kimono",
        "1f460" to "high_heels",
        "1f461" to "sandal",
        "1f462" to "boot",
        "1f45e" to "shoe",
        "1f45f" to "athletic_shoe",
        "1f452" to "hat",
        "1f3a9" to "top_hat",
        "1f393" to "graduate",
        "1f451" to "crown",
        "26d1" to "helmet",
        "1f392" to "backpack",
        "1f45d" to "pouch",
        "1f45b" to "purse",
        "1f45c" to "handbag",
        "1f4bc" to "briefcase",
        "1f453" to "glasses",
        "1f576" to "dark_sunglasses",
        "1f302" to "closed_umbrella",
        "2602" to "umbrella",
        "1f436" to "puppy",
        "1f431" to "kitten",
        "1f42d" to "dormouse",
        "1f439" to "hamster",
        "1f430" to "bunny",
        "1f98a" to "fox",
        "1f43b" to "bear",
        "1f43c" to "panda",
        "1f428" to "koala",
        "1f42f" to "tiger_cub",
        "1f981" to "lion",
        "1f42e" to "calf",
        "1f437" to "piglet",
        "1f43d" to "pig_nose",
        "1f438" to "frog",
        "1f435" to "monkey_face",
        "1f648" to "see_no_evil",
        "1f649" to "hear_no_evil",
        "1f64a" to "speak_no_evil",
        "1f412" to "monkey",
        "1f414" to "chicken",
        "1f427" to "penguin",
        "1f426" to "bird",
        "1f424" to "chick",
        "1f423" to "hatching",
        "1f425" to "new_baby",
        "1f986" to "duck",
        "1f985" to "eagle",
        "1f989" to "owl",
        "1f987" to "bat",
        "1f43a" to "wolf",
        "1f417" to "boar",
        "1f434" to "pony",
        "1f984" to "unicorn",
        "1f41d" to "bee",
        "1f41b" to "bug",
        "1f98b" to "butterfly",
        "1f40c" to "snail",
        "1f41a" to "shell",
        "1f41e" to "beetle",
        "1f41c" to "ant",
        "1f577" to "spider",
        "1f578" to "web",
        "1f422" to "turtle",
        "1f40d" to "snake",
        "1f98e" to "lizard",
        "1f982" to "scorpion",
        "1f980" to "crab",
        "1f991" to "squid",
        "1f419" to "octopus",
        "1f990" to "shrimp",
        "1f420" to "tropical_fish",
        "1f41f" to "fish",
        "1f421" to "blowfish",
        "1f42c" to "dolphin",
        "1f988" to "shark",
        "1f433" to "whale",
        "1f40b" to "humpback_whale",
        "1f40a" to "crocodile",
        "1f406" to "leopard",
        "1f405" to "tiger",
        "1f403" to "water_buffalo",
        "1f402" to "ox",
        "1f404" to "cow",
        "1f98c" to "deer",
        "1f42a" to "arabian_camel",
        "1f42b" to "camel",
        "1f418" to "elephant",
        "1f98f" to "rhinoceros",
        "1f98d" to "gorilla",
        "1f40e" to "horse",
        "1f416" to "pig",
        "1f410" to "goat",
        "1f40f" to "ram",
        "1f411" to "sheep",
        "1f415" to "dog",
        "1f429" to "poodle",
        "1f408" to "cat",
        "1f413" to "rooster",
        "1f983" to "turkey",
        "1f54a" to "dove",
        "1f407" to "rabbit",
        "1f401" to "mouse",
        "1f400" to "rat",
        "1f43f" to "chipmunk",
        "1f43e" to "paw_prints",
        "1f409" to "dragon",
        "1f432" to "dragon_face",
        "1f335" to "cactus",
        "1f384" to "holiday_tree",
        "1f332" to "evergreen_tree",
        "1f333" to "tree",
        "1f334" to "palm_tree",
        "1f331" to "seedling",
        "1f33f" to "herb",
        "2618" to "shamrock",
        "1f340" to "lucky",
        "1f38d" to "bamboo",
        "1f38b" to "wish_tree",
        "1f343" to "leaves",
        "1f342" to "fallen_leaf",
        "1f341" to "maple_leaf",
        "1f344" to "mushroom",
        "1f33e" to "harvest",
        "1f490" to "bouquet",
        "1f337" to "tulip",
        "1f339" to "rose",
        "1f940" to "wilted_flower",
        "1f33b" to "sunflower",
        "1f33c" to "blossom",
        "1f338" to "cherry_blossom",
        "1f33a" to "hibiscus",
        "1f30e" to "earth_americas",
        "1f30d" to "earth_africa",
        "1f30f" to "earth_asia",
        "1f315" to "full_moon",
        "1f311" to "new_moon",
        "1f314" to "waxing_moon",
        "1f31a" to "new_moon_face",
        "1f31d" to "moon_face",
        "1f31e" to "sun_face",
        "1f31b" to "goodnight",
        "1f319" to "moon",
        "1f4ab" to "seeing_stars",
        "2b50" to "star",
        "1f31f" to "glowing_star",
        "2728" to "sparkles",
        "26a1" to "high_voltage",
        "1f525" to "fire",
        "1f4a5" to "boom",
        "2604" to "comet",
        "2600" to "sunny",
        "1f324" to "mostly_sunny",
        "26c5" to "partly_sunny",
        "1f325" to "cloudy",
        "1f326" to "sunshowers",
        "1f308" to "rainbow",
        "2601" to "cloud",
        "1f327" to "rainy",
        "26c8" to "thunderstorm",
        "1f329" to "lightning",
        "1f328" to "snowy",
        "2603" to "snowman",
        "26c4" to "frosty",
        "2744" to "snowflake",
        "1f32c" to "windy",
        "1f4a8" to "dash",
        "1f32a" to "tornado",
        "1f32b" to "fog",
        "1f30a" to "ocean",
        "1f4a7" to "drop",
        "1f4a6" to "sweat_drops",
        "2614" to "umbrella_with_rain",
        "1f34f" to "green_apple",
        "1f34e" to "apple",
        "1f350" to "pear",
        "1f34a" to "orange",
        "1f34b" to "lemon",
        "1f34c" to "banana",
        "1f349" to "watermelon",
        "1f347" to "grapes",
        "1f353" to "strawberry",
        "1f348" to "melon",
        "1f352" to "cherries",
        "1f351" to "peach",
        "1f34d" to "pineapple",
        "1f95d" to "kiwi",
        "1f951" to "avocado",
        "1f345" to "tomato",
        "1f346" to "eggplant",
        "1f952" to "cucumber",
        "1f955" to "carrot",
        "1f33d" to "corn",
        "1f336" to "hot_pepper",
        "1f954" to "potato",
        "1f360" to "yam",
        "1f330" to "chestnut",
        "1f95c" to "peanuts",
        "1f36f" to "honey",
        "1f950" to "croissant",
        "1f35e" to "bread",
        "1f956" to "baguette",
        "1f9c0" to "cheese",
        "1f95a" to "egg",
        "1f373" to "cooking",
        "1f953" to "bacon",
        "1f95e" to "pancakes",
        "1f364" to "tempura",
        "1f357" to "drumstick",
        "1f356" to "meat",
        "1f355" to "pizza",
        "1f32d" to "hotdog",
        "1f354" to "hamburger",
        "1f35f" to "fries",
        "1f959" to "doner_kebab",
        "1f32e" to "taco",
        "1f32f" to "burrito",
        "1f957" to "salad",
        "1f958" to "paella",
        "1f35d" to "spaghetti",
        "1f35c" to "ramen",
        "1f372" to "food",
        "1f365" to "naruto",
        "1f363" to "sushi",
        "1f371" to "bento",
        "1f35b" to "curry",
        "1f35a" to "rice",
        "1f359" to "onigiri",
        "1f358" to "senbei",
        "1f362" to "oden",
        "1f361" to "dango",
        "1f367" to "shaved_ice",
        "1f368" to "ice_cream",
        "1f366" to "soft_serve",
        "1f370" to "cake",
        "1f382" to "birthday",
        "1f36e" to "custard",
        "1f36d" to "lollipop",
        "1f36c" to "candy",
        "1f36b" to "chocolate",
        "1f37f" to "popcorn",
        "1f369" to "donut",
        "1f36a" to "cookie",
        "1f95b" to "milk",
        "1f37c" to "baby_bottle",
        "2615" to "coffee",
        "1f375" to "tea",
        "1f376" to "sake",
        "1f37a" to "beer",
        "1f37b" to "beers",
        "1f942" to "clink",
        "1f377" to "wine",
        "1f943" to "small_glass",
        "1f378" to "cocktail",
        "1f379" to "tropical_drink",
        "1f37e" to "champagne",
        "1f944" to "spoon",
        "1f374" to "fork_and_knife",
        "1f37d" to "hungry",
        "26bd" to "football",
        "1f3c0" to "basketball",
        "1f3c8" to "american_football",
        "26be" to "baseball",
        "1f3be" to "tennis",
        "1f3d0" to "volleyball",
        "1f3c9" to "rugby",
        "1f3b1" to "billiards",
        "1f3d3" to "ping_pong",
        "1f3f8" to "badminton",
        "1f945" to "gooooooooal",
        "1f3d2" to "ice_hockey",
        "1f3d1" to "field_hockey",
        "1f3cf" to "cricket",
        "26f3" to "hole_in_one",
        "1f3f9" to "bow_and_arrow",
        "1f3a3" to "fishing",
        "1f94a" to "boxing_glove",
        "1f94b" to "black_belt",
        "26f8" to "ice_skate",
        "1f3bf" to "ski",
        "26f7" to "skier",
        "1f3c2" to "snowboarder",
        "1f3cb" to "lift",
        "1f93a" to "fencing",
        "1f93c" to "wrestling",
        "1f938" to "cartwheel",
        "26f9" to "ball",
        "1f93e" to "handball",
        "1f3cc" to "golf",
        "1f3c4" to "surf",
        "1f3ca" to "swim",
        "1f93d" to "water_polo",
        "1f6a3" to "rowboat",
        "1f3c7" to "horse_racing",
        "1f6b4" to "cyclist",
        "1f6b5" to "mountain_biker",
        "1f3bd" to "running_shirt",
        "1f3c5" to "medal",
        "1f396" to "military_medal",
        "1f947" to "first_place",
        "1f948" to "second_place",
        "1f949" to "third_place",
        "1f3c6" to "trophy",
        "1f3f5" to "rosette",
        "1f397" to "reminder_ribbon",
        "1f3ab" to "pass",
        "1f39f" to "ticket",
        "1f3aa" to "circus",
        "1f939" to "juggling",
        "1f3ad" to "performing_arts",
        "1f3a8" to "art",
        "1f3ac" to "action",
        "1f3a4" to "microphone",
        "1f3a7" to "headphones",
        "1f3bc" to "musical_score",
        "1f3b9" to "piano",
        "1f941" to "drum",
        "1f3b7" to "saxophone",
        "1f3ba" to "trumpet",
        "1f3b8" to "guitar",
        "1f3bb" to "violin",
        "1f3b2" to "dice",
        "1f3af" to "direct_hit",
        "1f3b3" to "strike",
        "1f3ae" to "video_game",
        "1f3b0" to "slot_machine",
        "1f697" to "car",
        "1f695" to "taxi",
        "1f699" to "recreational_vehicle",
        "1f68c" to "bus",
        "1f68e" to "trolley",
        "1f3ce" to "racecar",
        "1f693" to "police_car",
        "1f691" to "ambulance",
        "1f692" to "fire_truck",
        "1f690" to "minibus",
        "1f69a" to "moving_truck",
        "1f69b" to "truck",
        "1f69c" to "tractor",
        "1f6f4" to "kick_scooter",
        "1f6b2" to "bike",
        "1f6f5" to "scooter",
        "1f3cd" to "motorcycle",
        "1f6a8" to "siren",
        "1f694" to "oncoming_police_car",
        "1f68d" to "oncoming_bus",
        "1f698" to "oncoming_car",
        "1f696" to "oncoming_taxi",
        "1f6a1" to "aerial_tramway",
        "1f6a0" to "gondola",
        "1f69f" to "suspension_railway",
        "1f683" to "railway_car",
        "1f68b" to "tram",
        "1f69e" to "mountain_railway",
        "1f69d" to "monorail",
        "1f684" to "high_speed_train",
        "1f685" to "bullet_train",
        "1f688" to "light_rail",
        "1f682" to "train",
        "1f686" to "oncoming_train",
        "1f687" to "subway",
        "1f68a" to "oncoming_tram",
        "1f689" to "station",
        "1f681" to "helicopter",
        "1f6e9" to "small_airplane",
        "2708" to "airplane",
        "1f6eb" to "take_off",
        "1f6ec" to "landing",
        "1f680" to "rocket",
        "1f6f0" to "satellite",
        "1f4ba" to "seat",
        "1f6f6" to "canoe",
        "26f5" to "boat",
        "1f6e5" to "motor_boat",
        "1f6a4" to "speedboat",
        "1f6f3" to "passenger_ship",
        "26f4" to "ferry",
        "1f6a2" to "ship",
        "2693" to "anchor",
        "1f6a7" to "work_in_progress",
        "26fd" to "fuel_pump",
        "1f68f" to "bus_stop",
        "1f6a6" to "traffic_light",
        "1f6a5" to "horizontal_traffic_light",
        "1f5fa" to "map",
        "1f5ff" to "rock_carving",
        "1f5fd" to "statue",
        "26f2" to "fountain",
        "1f5fc" to "tower",
        "1f3f0" to "castle",
        "1f3ef" to "shiro",
        "1f3df" to "stadium",
        "1f3a1" to "ferris_wheel",
        "1f3a2" to "roller_coaster",
        "1f3a0" to "carousel",
        "26f1" to "beach_umbrella",
        "1f3d6" to "beach",
        "1f3dd" to "island",
        "26f0" to "mountain",
        "1f3d4" to "snowy_mountain",
        "1f5fb" to "mount_fuji",
        "1f30b" to "volcano",
        "1f3dc" to "desert",
        "1f3d5" to "campsite",
        "26fa" to "tent",
        "1f6e4" to "railway_track",
        "1f6e3" to "road",
        "1f3d7" to "construction",
        "1f3ed" to "factory",
        "1f3e0" to "house",
        "1f3e1" to "suburb",
        "1f3d8" to "houses",
        "1f3da" to "derelict_house",
        "1f3e2" to "office",
        "1f3ec" to "department_store",
        "1f3e3" to "japan_post",
        "1f3e4" to "post_office",
        "1f3e5" to "hospital",
        "1f3e6" to "bank",
        "1f3e8" to "hotel",
        "1f3ea" to "convenience_store",
        "1f3eb" to "school",
        "1f3e9" to "love_hotel",
        "1f492" to "wedding",
        "1f3db" to "classical_building",
        "26ea" to "church",
        "1f54c" to "mosque",
        "1f54d" to "synagogue",
        "1f54b" to "kaaba",
        "26e9" to "shinto_shrine",
        "1f5fe" to "japan",
        "1f391" to "moon_ceremony",
        "1f3de" to "national_park",
        "1f305" to "sunrise",
        "1f304" to "mountain_sunrise",
        "1f320" to "shooting_star",
        "1f387" to "sparkler",
        "1f386" to "fireworks",
        "1f307" to "city_sunrise",
        "1f306" to "sunset",
        "1f3d9" to "city",
        "1f303" to "night",
        "1f30c" to "milky_way",
        "1f309" to "bridge",
        "1f301" to "foggy",
        "231a" to "watch",
        "1f4f1" to "mobile_phone",
        "1f4f2" to "calling",
        "1f4bb" to "computer",
        "2328" to "keyboard",
        "1f5a5" to "desktop_computer",
        "1f5a8" to "printer",
        "1f5b1" to "computer_mouse",
        "1f5b2" to "trackball",
        "1f579" to "joystick",
        "1f5dc" to "compression",
        "1f4bd" to "gold_record",
        "1f4be" to "floppy_disk",
        "1f4bf" to "cd",
        "1f4c0" to "dvd",
        "1f4fc" to "vhs",
        "1f4f7" to "camera",
        "1f4f8" to "taking_a_picture",
        "1f4f9" to "video_camera",
        "1f3a5" to "movie_camera",
        "1f4fd" to "projector",
        "1f39e" to "film",
        "1f4de" to "landline",
        "260e" to "phone",
        "1f4df" to "pager",
        "1f4e0" to "fax",
        "1f4fa" to "tv",
        "1f4fb" to "radio",
        "1f399" to "studio_microphone",
        "1f39a" to "volume",
        "1f39b" to "control_knobs",
        "23f1" to "stopwatch",
        "23f2" to "timer",
        "23f0" to "alarm_clock",
        "1f570" to "mantelpiece_clock",
        "231b" to "times_up",
        "23f3" to "time_ticking",
        "1f4e1" to "satellite_antenna",
        "1f50b" to "battery",
        "1f50c" to "electric_plug",
        "1f4a1" to "light_bulb",
        "1f526" to "flashlight",
        "1f56f" to "candle",
        "1f5d1" to "wastebasket",
        "1f6e2" to "oil_drum",
        "1f4b8" to "losing_money",
        "1f4b5" to "dollar_bills",
        "1f4b4" to "yen_banknotes",
        "1f4b6" to "euro_banknotes",
        "1f4b7" to "pound_notes",
        "1f4b0" to "money",
        "1f4b3" to "credit_card",
        "1f48e" to "gem",
        "2696" to "justice",
        "1f527" to "fixing",
        "1f528" to "hammer",
        "2692" to "at_work",
        "1f6e0" to "working_on_it",
        "26cf" to "mine",
        "1f529" to "nut_and_bolt",
        "2699" to "gear",
        "26d3" to "chains",
        "1f52b" to "gun",
        "1f4a3" to "bomb",
        "1f52a" to "knife",
        "1f5e1" to "dagger",
        "2694" to "duel",
        "1f6e1" to "shield",
        "1f6ac" to "smoking",
        "26b0" to "coffin",
        "26b1" to "funeral_urn",
        "1f3fa" to "vase",
        "1f52e" to "crystal_ball",
        "1f4ff" to "prayer_beads",
        "1f488" to "barber",
        "2697" to "alchemy",
        "1f52d" to "telescope",
        "1f52c" to "science",
        "1f573" to "hole",
        "1f48a" to "medicine",
        "1f489" to "injection",
        "1f321" to "temperature",
        "1f6bd" to "toilet",
        "1f6b0" to "potable_water",
        "1f6bf" to "shower",
        "1f6c1" to "bathtub",
        "1f6c0" to "bath",
        "1f6ce" to "bellhop_bell",
        "1f511" to "key",
        "1f5dd" to "secret",
        "1f6aa" to "door",
        "1f6cb" to "living_room",
        "1f6cf" to "bed",
        "1f6cc" to "in_bed",
        "1f5bc" to "picture",
        "1f6cd" to "shopping_bags",
        "1f6d2" to "shopping_cart",
        "1f381" to "gift",
        "1f388" to "balloon",
        "1f38f" to "carp_streamer",
        "1f380" to "ribbon",
        "1f38a" to "confetti",
        "1f389" to "tada",
        "1f38e" to "dolls",
        "1f3ee" to "lantern",
        "1f390" to "wind_chime",
        "2709" to "email",
        "1f4e9" to "mail_sent",
        "1f4e8" to "mail_received",
        "1f4e7" to "e-mail",
        "1f48c" to "love_letter",
        "1f4e5" to "inbox",
        "1f4e4" to "outbox",
        "1f4e6" to "package",
        "1f3f7" to "label",
        "1f4ea" to "closed_mailbox",
        "1f4eb" to "mailbox",
        "1f4ec" to "unread_mail",
        "1f4ed" to "inbox_zero",
        "1f4ee" to "mail_dropoff",
        "1f4ef" to "horn",
        "1f4dc" to "scroll",
        "1f4c3" to "receipt",
        "1f4c4" to "document",
        "1f4d1" to "place_holder",
        "1f4ca" to "bar_chart",
        "1f4c8" to "chart",
        "1f4c9" to "downwards_trend",
        "1f5d2" to "spiral_notepad",
        "1f4c6" to "date",
        "1f4c5" to "calendar",
        "1f4c7" to "rolodex",
        "1f5c3" to "archive",
        "1f5f3" to "ballot_box",
        "1f5c4" to "file_cabinet",
        "1f4cb" to "clipboard",
        "1f4c1" to "organize",
        "1f4c2" to "folder",
        "1f5c2" to "sort",
        "1f5de" to "newspaper",
        "1f4f0" to "headlines",
        "1f4d3" to "notebook",
        "1f4d4" to "decorative_notebook",
        "1f4d2" to "ledger",
        "1f4d5" to "red_book",
        "1f4d7" to "green_book",
        "1f4d8" to "blue_book",
        "1f4d9" to "orange_book",
        "1f4da" to "books",
        "1f4d6" to "book",
        "1f516" to "bookmark",
        "1f517" to "link",
        "1f4ce" to "paperclip",
        "1f587" to "office_supplies",
        "1f4d0" to "carpenter_square",
        "1f4cf" to "ruler",
        "1f4cc" to "push_pin",
        "1f4cd" to "pin",
        "2702" to "scissors",
        "1f58a" to "pen",
        "1f58b" to "fountain_pen",
        "1f58c" to "paintbrush",
        "1f58d" to "crayon",
        "1f4dd" to "memo",
        "270f" to "pencil",
        "1f50d" to "search",
        "1f50f" to "privacy",
        "1f510" to "secure",
        "1f512" to "locked",
        "1f513" to "unlocked",
        "2764" to "heart",
        "1f49b" to "yellow_heart",
        "1f49a" to "green_heart",
        "1f499" to "blue_heart",
        "1f49c" to "purple_heart",
        "1f5a4" to "black_heart",
        "1f494" to "broken_heart",
        "2763" to "heart_exclamation",
        "1f495" to "two_hearts",
        "1f49e" to "revolving_hearts",
        "1f493" to "heartbeat",
        "1f497" to "heart_pulse",
        "1f496" to "sparkling_heart",
        "1f498" to "cupid",
        "1f49d" to "gift_heart",
        "1f49f" to "heart_box",
        "262e" to "peace",
        "271d" to "cross",
        "262a" to "star_and_crescent",
        "1f549" to "om",
        "2638" to "wheel_of_dharma",
        "2721" to "star_of_david",
        "1f54e" to "menorah",
        "262f" to "yin_yang",
        "2626" to "orthodox_cross",
        "1f6d0" to "place_of_worship",
        "26ce" to "ophiuchus",
        "2648" to "aries",
        "2649" to "taurus",
        "264a" to "gemini",
        "264b" to "cancer",
        "264c" to "leo",
        "264d" to "virgo",
        "264e" to "libra",
        "264f" to "scorpius",
        "2650" to "sagittarius",
        "2651" to "capricorn",
        "2652" to "aquarius",
        "2653" to "pisces",
        "1f194" to "id",
        "269b" to "atom",
        "2622" to "radioactive",
        "2623" to "biohazard",
        "1f4f4" to "phone_off",
        "1f4f3" to "vibration_mode",
        "1f236" to "japanese_not_free_of_charge_button",
        "1f250" to "japanese_bargain_button",
        "1f251" to "japanese_acceptable_button",
        "1f21a" to "japanese_free_of_charge_button",
        "1f238" to "japanese_application_button",
        "1f23a" to "japanese_open_for_business_button",
        "1f237" to "japanese_monthly_amount_button",
        "3299" to "japanese_secret_button",
        "3297" to "japanese_congratulations_button",
        "1f234" to "japanese_passing_grade_button",
        "1f235" to "japanese_no_vacancy_button",
        "1f239" to "japanese_discount_button",
        "1f232" to "japanese_prohibited_button",
        "2734" to "eight_pointed_star",
        "1f19a" to "vs",
        "1f4ae" to "white_flower",
        "1f170" to "a",
        "1f171" to "b",
        "1f18e" to "ab",
        "1f191" to "cl",
        "1f17e" to "o",
        "1f198" to "sos",
        "274c" to "cross_mark",
        "2b55" to "circle",
        "1f6d1" to "stop_sign",
        "26d4" to "no_entry",
        "1f4db" to "name_badge",
        "1f6ab" to "prohibited",
        "1f4af" to "100",
        "1f4a2" to "anger",
        "2668" to "hot_springs",
        "1f6b7" to "no_pedestrians",
        "1f6af" to "do_not_litter",
        "1f6b3" to "no_bicycles",
        "1f6b1" to "non-potable_water",
        "1f51e" to "underage",
        "1f4f5" to "no_phones",
        "1f6ad" to "no_smoking",
        "2757" to "exclamation",
        "2755" to "grey_exclamation",
        "2753" to "question",
        "2754" to "grey_question",
        "203c" to "bangbang",
        "2049" to "interrobang",
        "1f505" to "low_brightness",
        "1f506" to "brightness",
        "303d" to "part_alternation",
        "26a0" to "warning",
        "1f6b8" to "children_crossing",
        "1f531" to "trident",
        "269c" to "fleur_de_lis",
        "1f530" to "beginner",
        "267b" to "recycle",
        "2705" to "check",
        "1f4b9" to "stock_market",
        "2747" to "sparkle",
        "2733" to "eight_spoked_asterisk",
        "274e" to "x",
        "1f310" to "www",
        "1f4a0" to "cute",
        "24c2" to "metro",
        "1f300" to "cyclone",
        "1f4a4" to "zzz",
        "1f3e7" to "atm",
        "1f6be" to "wc",
        "267f" to "accessible",
        "1f17f" to "parking",
        "1f6c2" to "passport_control",
        "1f6c3" to "customs",
        "1f6c4" to "baggage_claim",
        "1f6c5" to "locker",
        "1f6b9" to "mens",
        "1f6ba" to "womens",
        "1f6bc" to "baby_change_station",
        "1f6bb" to "restroom",
        "1f6ae" to "put_litter_in_its_place",
        "1f3a6" to "cinema",
        "1f4f6" to "cell_reception",
        "1f523" to "symbols",
        "2139" to "info",
        "1f524" to "abc",
        "1f521" to "abcd",
        "1f520" to "capital_abcd",
        "1f196" to "ng",
        "1f197" to "squared_ok",
        "1f199" to "squared_up",
        "1f192" to "cool",
        "1f195" to "new",
        "1f193" to "free",
        "0030-20e3" to "zero",
        "0031-20e3" to "one",
        "0032-20e3" to "two",
        "0033-20e3" to "three",
        "0034-20e3" to "four",
        "0035-20e3" to "five",
        "0036-20e3" to "six",
        "0037-20e3" to "seven",
        "0038-20e3" to "eight",
        "0039-20e3" to "nine",
        "1f51f" to "ten",
        "1f522" to "1234",
        "0023-20e3" to "hash",
        "002a-20e3" to "asterisk",
        "25b6" to "play",
        "23f8" to "pause",
        "23ef" to "play_pause",
        "23f9" to "stop_button",
        "23fa" to "record",
        "23ed" to "next_track",
        "23ee" to "previous_track",
        "23e9" to "fast_forward",
        "23ea" to "rewind",
        "23eb" to "double_up",
        "23ec" to "double_down",
        "25c0" to "play_reverse",
        "1f53c" to "upvote",
        "1f53d" to "downvote",
        "27a1" to "right",
        "2b05" to "left",
        "2b06" to "up",
        "2b07" to "down",
        "2197" to "upper_right",
        "2198" to "lower_right",
        "2199" to "lower_left",
        "2196" to "upper_left",
        "2195" to "up_down",
        "2194" to "left_right",
        "21aa" to "forward",
        "21a9" to "reply",
        "2934" to "heading_up",
        "2935" to "heading_down",
        "1f500" to "shuffle",
        "1f501" to "repeat",
        "1f502" to "repeat_one",
        "1f504" to "counterclockwise",
        "1f503" to "clockwise",
        "1f3b5" to "music",
        "1f3b6" to "musical_notes",
        "2795" to "plus",
        "2796" to "minus",
        "2797" to "division",
        "2716" to "multiplication",
        "1f4b2" to "dollars",
        "1f4b1" to "exchange",
        "2122" to "tm",
        "3030" to "wavy_dash",
        "27b0" to "loop",
        "27bf" to "double_loop",
        "1f51a" to "end",
        "1f519" to "back",
        "1f51b" to "on",
        "1f51d" to "top",
        "1f51c" to "soon",
        "2714" to "check_mark",
        "2611" to "checkbox",
        "1f518" to "radio_button",
        "26aa" to "white_circle",
        "26ab" to "black_circle",
        "1f534" to "red_circle",
        "1f535" to "blue_circle",
        "1f53a" to "red_triangle_up",
        "1f53b" to "red_triangle_down",
        "1f538" to "small_orange_diamond",
        "1f539" to "small_blue_diamond",
        "1f536" to "large_orange_diamond",
        "1f537" to "large_blue_diamond",
        "1f533" to "black_and_white_square",
        "1f532" to "white_and_black_square",
        "25aa" to "black_small_square",
        "25ab" to "white_small_square",
        "25fe" to "black_medium_small_square",
        "25fd" to "white_medium_small_square",
        "25fc" to "black_medium_square",
        "25fb" to "white_medium_square",
        "2b1b" to "black_large_square",
        "2b1c" to "white_large_square",
        "1f508" to "speaker",
        "1f507" to "mute",
        "1f509" to "softer",
        "1f50a" to "louder",
        "1f514" to "notifications",
        "1f515" to "mute_notifications",
        "1f4e3" to "megaphone",
        "1f4e2" to "loudspeaker",
        "1f4ac" to "umm",
        "1f5e8" to "speech_bubble",
        "1f4ad" to "thought",
        "1f5ef" to "anger_bubble",
        "2660" to "spades",
        "2663" to "clubs",
        "2665" to "hearts",
        "2666" to "diamonds",
        "1f0cf" to "joker",
        "1f3b4" to "playing_cards",
        "1f004" to "mahjong",
        "1f557" to "time",
        "1f3f3" to "white_flag",
        "1f3f4" to "black_flag",
        "1f3c1" to "checkered_flag",
        "1f6a9" to "triangular_flag",
        "1f38c" to "crossed_flags"
    ).map {
        combineStringToEmoji(it.key) to it.value
    }.toMap()

    private val invertEmojiMap = emojiMap.map { it.value to it.key }.toMap()
}