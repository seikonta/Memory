package app.takasu.kon.memory

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// DataBaseに保存するデータの宣言
open class Memo (
    @PrimaryKey open var id: String = UUID.randomUUID().toString(),
    open var imageUriString: String? = null,
    open var title: String? = null,
    open var content: String? = null,
    open var createdAt: Date = Date(System.currentTimeMillis())
) : RealmObject()