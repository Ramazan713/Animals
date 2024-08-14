package com.masterplus.animals.core.presentation.utils

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.AnimalDetail
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.domain.models.GenusModel
import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.domain.models.OrderModel
import com.masterplus.animals.core.domain.models.PhylumModel
import com.masterplus.animals.core.domain.models.SpeciesDetail
import com.masterplus.animals.core.domain.models.SpeciesImageModel
import com.masterplus.animals.core.domain.models.SpeciesModel
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel
import com.masterplus.animals.core.shared_features.auth.domain.enums.AuthProviderType
import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import com.masterplus.animals.core.shared_features.list.domain.models.SelectableListView
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.features.settings.presentation.link_accounts.models.LinkAccountModel
import com.masterplus.animals.features.species_detail.presentation.models.TitleContentModel
import com.masterplus.animals.features.species_detail.presentation.models.TitleSectionModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object SampleDatas {
    const val imageUrl = "https://storage.googleapis.com/animals-ce701.appspot.com/public/images/class/mammalia.jpg?Expires=1724517659&GoogleAccessId=firebase-adminsdk-hprer%40animals-ce701.iam.gserviceaccount.com&Signature=N27yfyDPC7RZzh0Cirowi0Ki%2FisODA%2Bnp9i2dAjR5OEDZV8Cuji0CwMOqhZoQYE9QnD8LOcNI2vR6uJ9ChzUhFrOJ9BmKzroiwzqhRsU1OgFYQwCpE5yi9WFPwpw3Pmlaz%2B1dIr6I3%2BQAfM91y6kMPpFFPwKFwT4H7uS%2FYWEu91x0VLvWH%2FMPXGiYmIcNEQnhigPRT%2BPsjxJyAwoajEOSGgHrXai43%2FtiXoZYYVMbLOOR03BKNcMC4dGC56S6zA05JnjplmZ%2Bn2veofgUGocGOeQ0kL9BngIDjlbv6wS7gxZlHbp4gJEG3UF3HdGi3thfImt3W%2FUthn0dJlpPPcz1g%3D%3D"


    val speciesImageModel = SpeciesImageModel(
        id = 1,
        speciesId = 2,
        name = null,
        imageUrl = imageUrl,
        imagePath = "",
        imageOrder = 1,
        createdAt = "",
        updatedAt = ""
    )

    val imageWithTitleModel1 = ImageWithTitleModel(
        title = "Kartal",
        id = 1,
        imageUrl = imageUrl
    )
    val imageWithTitleModel2 = ImageWithTitleModel(
        title = "Kartal",
        subTitle = "Sub Title",
        id = 2,
        imageUrl = imageUrl
    )

    val categoryData = CategoryData(
        title = "Kartal",
        secondaryTitle = "Sub Title",
        id = 2,
        imageUrl = imageUrl,
        categoryType = CategoryType.Class
    )

    val phylum = PhylumModel(
        id = 2,
        scientificName = "Arthropoda",
        phylum = "Arthropoda",
        kingdomId = 1
    )
    val classModel = ClassModel(
        id = 2,
        scientificName = "Insecta",
        className = "",
        phylumId = 2,
        imageUrl = "",
        imagePath = ""
    )
    val order = OrderModel(
        id = 2,
        scientificName = "Hymenoptera",
        order = "",
        classId = 2,
        imageUrl = "",
        imagePath = ""
    )

    val family = FamilyModel(
        id = 2,
        scientificName = "Apidae",
        family = "",
        orderId = 2,
        imageUrl = "",
        imagePath = ""
    )

    val genus = GenusModel(
        id = 2,
        scientificName = "Apis",
        genus = "",
        familyId = 2,
    )

    val species = SpeciesModel(
        id = 2,
        scientificName = "Apis mellifera",
        introduction="Arılar, dünya genelinde tarımın ve doğal ekosistemlerin sürdürülebilirliği için hayati öneme sahip polinatörlerdir. Bal üretimi ile tanınsalar da, bitkilerin tozlaşmasını sağlayarak birçok meyve ve sebzenin oluşmasında kritik rol oynarlar. Arılar, karmaşık sosyal yapıları ve etkileyici iş bölümleriyle de dikkat çekerler. Özellikle bal arıları, ürettikleri bal ve balmumu ile insanlık için ekonomik değere sahiptir.",
        name ="Bal Arısı",
        habitatCategoryId=1,
        genusId=2,
        recognitionAndInteraction=8,
    )

    val habitatCategory = HabitatCategoryModel(
        id = 1,
        habitatCategory = "Karasal"
    )

    val animalImage = SpeciesImageModel(
        id = 1,
        imagePath = "",
        imageUrl = "",
        speciesId = 2,
        imageOrder = 1,
        name = null,
        updatedAt = "",
        createdAt = ""
    )



    val animal = Animal(
        id=2,
        species = species,
        images = listOf(speciesImageModel),
        physicalCharacteristics="Bal arıları, sarı ve siyah çizgili vücutları ve kıllı yapıları ile tanınırlar. Kanatları saydamdır ve vücutlarının arka kısmında bir iğne bulunur. İşçi arılar, kraliçeye göre daha küçük olup, yaklaşık 12-15 mm uzunluğundadır. Kraliçe arı ise yaklaşık 20 mm uzunluğunda olabilir.",
        naturalHabitat="Arılar, ormanlar, bahçeler, tarım alanları ve hatta şehirlerdeki parklarda ve bahçelerde yaşarlar. Çiçekli bitkilerin bol olduğu bölgelerde yoğun olarak bulunurlar.",
        ecosystem="Arılar, polinatör olarak ekosistemlerin sağlığı için kritik bir rol oynar. Çiçekli bitkilerin tozlaşmasını sağlayarak bitkilerin üremesine yardımcı olurlar, bu da gıda zincirinin temelini oluşturur.",
        feedingHabits="Arılar, nektar ve polen ile beslenirler. Nektar, enerji sağlamak için şeker içerirken, polen protein ve diğer besin maddeleri sağlar. İşçi arılar, bu kaynakları toplamak için çiçeklere uçar ve böylece tozlaşma sürecine katkıda bulunurlar.",
        socialStructure="Bal arıları, oldukça organize kolonilerde yaşar. Bir kolonide bir kraliçe, binlerce işçi arı ve birkaç yüz erkek arı (drone) bulunur. Kraliçe arı, yumurtlama göreviyle koloninin devamlılığını sağlar, işçi arılar ise yiyecek toplama, yavru bakımı ve kovanın temizliği gibi görevleri üstlenir.",
        reproductiveBehaviors="Kraliçe arı, koloninin tek yumurtlayan bireyidir ve hayatı boyunca milyonlarca yumurta bırakabilir. Erkek arılar kraliçe ile çiftleşir, ardından genellikle ölürler. Kraliçe arı, döllenmiş yumurtalardan işçi arılar ve yeni kraliçeler, döllenmemiş yumurtalardan ise erkek arılar üretir.",
        developmentStages="Yumurtadan çıkan larvalar, işçi arılar tarafından beslenir ve bakılır. Larvalar birkaç kez deri değiştirir ve sonunda pupaya dönüşür. Birkaç gün içinde yetişkin bir arı olarak kovandan çıkarlar ve çalışmaya başlarlar.",
        soundsProduced="Arılar, kovan içinde vızıltı şeklinde sesler çıkararak iletişim kurarlar. Ayrıca, kraliçe arının özel bir sesi olan 'piping' sesi, kovan içindeki diğer arılar için önemli sinyaller taşır.",
        communicationMethods="Arılar, dans dili adı verilen karmaşık hareketlerle iletişim kurarlar. Özellikle işçi arılar, diğer arılara nektar kaynaklarının yerini tarif etmek için 'wagtail dansı' yaparlar. Bu dans, nektar kaynağının uzaklığı ve yönü hakkında bilgi verir.",
        threats="Arılar, pestisitler, habitat kaybı, hastalıklar ve parazitler nedeniyle tehdit altındadır. Özellikle varroa akarı ve koloni çöküş bozukluğu, arı popülasyonları üzerinde büyük bir etkiye sahiptir.",
        conservationEfforts="Arıların korunması için çeşitli çabalar yürütülmektedir. Organik tarım yöntemlerinin teşvik edilmesi, pestisit kullanımının azaltılması ve arı habitatlarının korunması, bu çabalar arasında yer almaktadır.",
        culturalSignificance="Arılar, binlerce yıldır insanlar için önemli bir yer tutmuştur. Antik Mısır'dan Yunan mitolojisine kadar, arılar pek çok kültürde simgesel bir öneme sahiptir. Bal, hem gıda hem de ilaç olarak kullanılmıştır.",
        economicImportance="Bal arıları, tarımsal üretim için kritik bir rol oynar. Tozlaşma, birçok meyve ve sebzenin verimliliğini artırır. Ayrıca, bal ve balmumu üretimi, arıcılık sektörünün ekonomik değerini oluşturur. Bilimsel araştırmalar, arıların davranışları ve ekolojisi hakkında önemli bilgiler sağlamaktadır.",
        environmentalAdaptations="Arılar, karmaşık görme yetenekleri ve hassas antenleri sayesinde çiçekleri kolayca bulabilirler. Ayrıca, kovanlarını korumak ve sıcaklık düzenlemesi yapmak için etkili yöntemler geliştirmişlerdir.",
        evolutionaryProcesses="Arılar, sosyal davranışları ve iş bölümü ile evrimsel bir başarı örneğidir. Milyonlarca yıl süren evrimsel süreçte, karmaşık sosyal yapıları ve polinasyon becerileri sayesinde hayatta kalmayı başarmışlardır.",
        interestingBehaviors="Arılar, dans dili ile iletişim kurarak diğer arılara nektar ve polen kaynaklarını tarif ederler. Ayrıca, kovan savunması sırasında topluca saldırıya geçer ve düşmanlarına karşı kendilerini savunurlar.",
        funFacts="Bir arı, hayatı boyunca yaklaşık bir çay kaşığı bal üretebilir. Ayrıca, arılar geri geri uçabilen nadir böceklerdendir ve dans ederek mesafe ve yön bilgisini aktarabilirler.",
        size="12-20 mm",
        weight="0.1 gram",
        color="Sarı ve siyah çizgili",
        habitat="Ormanlar, bahçeler, tarım alanları",
        ecosystemCategory="Çiçekli bitkilerin bulunduğu alanlar",
        feeding="Nektar ve polen",
        socialStructureSimple="Sosyal, koloniler halinde yaşar",
        reproductiveSimple="Kraliçe arı yumurtlar, erkek arılar çiftleşir",
        developmentSimple="Larvalar beslenir ve pupaya dönüşür",
        sounds="Vızıltı, piping",
        communication="Dans dili, vızıltı",
        threatsSimple="Pestisitler, habitat kaybı, hastalıklar",
        conservationStatus="Tehdit altında",
        culturalSimple="Mitoloji ve folklorda önemli yer tutar",
        economicSimple="Tozlaşma ve bal üretimi",
        adaptation="Karmaşık görme ve anten kullanımı",
        evolution="Sosyal yapı ve iş bölümü",
        comparativeAnalysis = "Bal arıları, diğer tozlayıcılardan (örneğin, kelebekler) farklı olarak, poleni taşımak için özel olarak evrimleşmiş vücut yapılarıyla dikkat çekerler. Ayrıca, diğer arı türlerinden daha karmaşık bir sosyal yapıya sahiptirler.",
        conservationChallenges = "",
        modernDayPerception = "",
        ethologicalInsights = ""
    )

    val animalDetail = AnimalDetail(
        animal = animal,
        phylum = phylum,
        classModel = classModel,
        order = order,
        family = family,
        genus = genus,
        species = species,
        habitatCategory = habitatCategory,
        images = listOf(animalImage, animalImage, animalImage)
    )

    val titleSectionModel = TitleSectionModel(
        sectionTitle = "Yaşam Alanı",
        titleContents = listOf(
            TitleContentModel(
                title = "Doğal Habitat",
                content = animal.naturalHabitat
            ),
            TitleContentModel(
                title = "Ekosistem",
                content = animal.ecosystem
            )
        ),
        imageUrl = imageUrl
    )

    fun generateSavePoint(
        id: Int = 1,
        title: String = "Title $id",
        itemPosIndex: Int = 5,
        savePointDestination: SavePointDestination = SavePointDestination.ListType(1),
        modifiedDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    ): SavePoint {
        return SavePoint(id, title, SavePointContentType.Content,savePointDestination, itemPosIndex, modifiedDate,
            imageUrl,null)
    }

    fun generateLinkAccountModel(
        providerType: AuthProviderType = AuthProviderType.Email,
        isConnected: Boolean = true,
        title: UiText = providerType.title,
        info: UiText? = null
    ): LinkAccountModel {
        return LinkAccountModel(
            providerType = providerType,
            isConnected = isConnected,
            title = title,
            info = info
        )
    }

    fun generateSpeciesDetail(
        id: Int = 1,
        name: String = "Bal Arısi",
        scientificName: String = "Apis mellifera",
        introduction: String = "Arılar, dünya genelinde tarımın ve doğal ekosistemlerin sürdürülebilirliği için hayati öneme sahip polinatörlerdir. Bal üretimi ile tanınsalar da, bitkilerin tozlaşmasını sağlayarak birçok meyve ve sebzenin oluşmasında kritik rol oynarlar. Arılar, karmaşık sosyal yapıları ve etkileyici iş bölümleriyle de dikkat çekerler. Özellikle bal arıları, ürettikleri bal ve balmumu ile insanlık için ekonomik değere sahiptir.",
        imageUrls: List<SpeciesImageModel> = listOf(speciesImageModel),
        isFavorited: Boolean = false,
        isListSelected: Boolean = false,
        habitatCategoryId: Int = 1
    ): SpeciesDetail{
        return SpeciesDetail(
            id = id,
            name = name,
            scientificName = scientificName,
            introduction = introduction,
            images = imageUrls,
            isFavorited = isFavorited,
            isListSelected = isListSelected,
            habitatCategoryId = habitatCategoryId,
            recognitionAndInteraction = 1,
            genusId = 1
        )
    }

    fun generateListView(
        id: Int = 1,
        name: String = "list item $id",
        isRemovable: Boolean = false,
        isArchive: Boolean = false,
        listPos: Int = 1,
        contentMaxPos: Int = 1,
        itemCounts: Int = 2
    ): ListView{
        return ListView(id, name, isRemovable, isArchive, listPos, contentMaxPos, itemCounts)
    }

    fun generateSelectableListView(
        listView: ListView = generateListView(),
        isSelected: Boolean = false
    ): SelectableListView{
        return SelectableListView(listView, isSelected)
    }


    val selectableListViewArr = listOf(
        generateSelectableListView(generateListView(id = 1)),
        generateSelectableListView(generateListView(id = 2)),
        generateSelectableListView(generateListView(id = 3))
    )

    val listViewFavorite = generateListView(id = 1, isRemovable = false)
    val listView = generateListView(id = 2, isRemovable = true)
}