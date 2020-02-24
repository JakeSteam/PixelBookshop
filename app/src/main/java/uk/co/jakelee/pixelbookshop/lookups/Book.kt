package uk.co.jakelee.pixelbookshop.lookups

import uk.co.jakelee.pixelbookshop.interfaces.Model

enum class Book(
    override val id: Int,
    val genre: BookGenre,
    val rarity: BookRarity,
    val title: String,
    val authorFirstName: String,
    val authorSurname: String,
    val published: Int,
    val url: String
): Model {
    Orwell1984(40961427, BookGenre.ScienceFiction, BookRarity.Common, "1984", "George", "Orwell", 1949, "https://www.goodreads.com/book/show/40961427-1984"),
    TwainTheAdventuresofHuckleberryFinn(2956, BookGenre.HistoricalFiction, BookRarity.Common, "The Adventures of Huckleberry Finn", "Mark", "Twain", 1876, "https://www.goodreads.com/book/show/2956.The_Adventures_of_Huckleberry_Finn"),
    CarrollAlicesAdventuresinWonderland(6324090, BookGenre.Fantasy, BookRarity.Common, "Alice's Adventures in Wonderland", "Lewis", "Carroll", 1865, "https://www.goodreads.com/book/show/6324090-alice-s-adventures-in-wonderland"),
    RemarqueAllQuietontheWesternFront(355697, BookGenre.HistoricalFiction, BookRarity.Common, "All Quiet on the Western Front", "Erich Maria", "Remarque", 1928, "https://www.goodreads.com/book/show/355697.All_Quiet_on_the_Western_Front"),
    GaimanAmericanGods(30165203, BookGenre.Fantasy, BookRarity.Common, "American Gods", "Neil", "Gaiman", 2001, "https://www.goodreads.com/book/show/30165203-american-gods"),
    EllisAmericanPsycho(28676, BookGenre.Horror, BookRarity.Common, "American Psycho", "Bret Easton", "Ellis", 1991, "https://www.goodreads.com/book/show/28676.American_Psycho"),
    OrwellAnimalFarm(170448, BookGenre.Fantasy, BookRarity.Common, "Animal Farm", "George", "Orwell", 1945, "https://www.goodreads.com/book/show/170448.Animal_Farm"),
    TolstoyAnnaKarenina(15823480, BookGenre.Romance, BookRarity.Common, "Anna Karenina", "Leo", "Tolstoy", 1877, "https://www.goodreads.com/book/show/15823480-anna-karenina"),
    TzuTheArtofWar(10534, BookGenre.Reference, BookRarity.Common, "The Art of War", "Sun", "Tzu", -500, "https://www.goodreads.com/book/show/10534.The_Art_of_War"),
    GoldacreBadScience(3272165, BookGenre.Reference, BookRarity.Uncommon, "Bad Science", "Ben", "Goldacre", 2008, "https://www.goodreads.com/book/show/3272165-bad-science"),

    FaulksBirdsong(6259, BookGenre.HistoricalFiction, BookRarity.Uncommon, "Birdsong", "Sebastian", "Faulks", 1993, "https://www.goodreads.com/book/show/6259.Birdsong"),
    BoyneTheBoyintheStripedPyjamas(1754278, BookGenre.HistoricalFiction, BookRarity.Common, "The Boy in the Striped Pyjamas", "John", "Boyne", 2006, "https://www.goodreads.com/book/show/1754278.The_Boy_in_the_Striped_Pyjamas"),
    FieldingBridgetJonessDiary(227443, BookGenre.Romance, BookRarity.Common, "Bridget Jones's Diary", "Helen", "Fielding", 1996, "https://www.goodreads.com/book/show/227443.Bridget_Jones_s_Diary"),
    HawkingABriefHistoryofTime(3869, BookGenre.Reference, BookRarity.Common, "A Brief History of Time", "Stephen", "Hawking", 1988, "https://www.goodreads.com/book/show/3869.A_Brief_History_of_Time"),
    SalingerTheCatcherintheRye(5107, BookGenre.Drama, BookRarity.Common, "The Catcher in the Rye", "J.D.", "Salinger", 1951, "https://www.goodreads.com/book/show/5107.The_Catcher_in_the_Rye"),
    LewisTheChroniclesofNarnia(11127, BookGenre.Fantasy, BookRarity.Common, "The Chronicles of Narnia", "C.S.", "Lewis", 1949, "https://www.goodreads.com/book/show/11127.The_Chronicles_of_Narnia"),
    WalkerTheColorPurple(11486, BookGenre.HistoricalFiction, BookRarity.Common, "The Color Purple", "Alice", "Walker", 1982, "https://www.goodreads.com/book/show/11486.The_Color_Purple"),
    DoyleTheCommitments(269795, BookGenre.Humor, BookRarity.Uncommon, "The Commitments", "Roddy", "Doyle", 1987, "https://www.goodreads.com/book/show/269795.The_Commitments"),
    DumasTheCountofMonteCristo(7126, BookGenre.HistoricalFiction, BookRarity.Common, "The Count of Monte Cristo", "Alexandre", "Dumas", 1844, "https://www.goodreads.com/book/show/7126.The_Count_of_Monte_Cristo"),
    DostoyevskyCrimeandPunishment(7144, BookGenre.Crime, BookRarity.Common, "Crime and Punishment", "Fyodor", "Dostoyevsky", 1866, "https://www.goodreads.com/book/show/7144.Crime_and_Punishment"),

    FrankTheDiaryofaYoungGirl(48855, BookGenre.Biography, BookRarity.Common, "The Diary of a Young Girl", "Anne", "Frank", 1947, "https://www.goodreads.com/book/show/48855.The_Diary_of_a_Young_Girl"),
    SansomDissolution(138685, BookGenre.HistoricalFiction, BookRarity.Uncommon, "Dissolution", "C.J.", "Sansom", 2003, "https://www.goodreads.com/book/show/138685.Dissolution"),
    DickDoAndroidsDreamofElectricSheep(36402034, BookGenre.ScienceFiction, BookRarity.Common, "Do Androids Dream of Electric Sheep?", "Philip K.", "Dick", 1968, "https://www.goodreads.com/book/show/36402034-do-androids-dream-of-electric-sheep"),
    StokerDracula(17245, BookGenre.Horror, BookRarity.Common, "Dracula", "Bram", "Stoker", 1897, "https://www.goodreads.com/book/show/17245.Dracula"),
    BlytonTheEnchantedWood(17491, BookGenre.Fantasy, BookRarity.Uncommon, "The Enchanted Wood", "Enid", "Blyton", 1936, "https://www.goodreads.com/book/show/17491.The_Enchanted_Wood"),
    OndaatjeTheEnglishPatient(11713, BookGenre.HistoricalFiction, BookRarity.Common, "The English Patient", "Michael", "Ondaatje", 1992, "https://www.goodreads.com/book/show/11713.The_English_Patient"),
    ThompsonFearandLoathinginLasVegas(7745, BookGenre.Humor, BookRarity.Common, "Fear and Loathing in Las Vegas", "Hunter S.", "Thompson", 1971, "https://www.goodreads.com/book/show/7745.Fear_and_Loathing_in_Las_Vegas"),
    TolkienTheFellowshipoftheRing(34, BookGenre.Fantasy, BookRarity.Common, "The Fellowship of the Ring", "J.R.R.", "Tolkien", 1954, "https://www.goodreads.com/book/show/34.The_Fellowship_of_the_Ring"),
    KeyesFlowersforAlgernon(36576608, BookGenre.ScienceFiction, BookRarity.Common, "Flowers for Algernon", "Daniel", "Keyes", 1966, "https://www.goodreads.com/book/show/36576608-flowers-for-algernon"),
    ShelleyFrankenstein(35031085, BookGenre.Horror, BookRarity.Common, "Frankenstein", "Mary Wollstonecraft", "Shelley", 1818, "https://www.goodreads.com/book/show/35031085-frankenstein"),
}