package app.takasu.kon.memory

import io.realm.RealmObject

// DataBaseに保存するデータの宣言
open class Memo (
    open var tag: Int? = null,
    open var imageUriString: String? = null,
    open var title: String? = null,
    open var content: String? = null
) : RealmObject()