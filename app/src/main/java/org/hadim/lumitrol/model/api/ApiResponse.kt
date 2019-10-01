package org.hadim.lumitrol.model.api

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


interface ApiResponse {
    var result: String?
}

@Root(name = "camrply", strict = false)
abstract class ApiResponseBase : ApiResponse {
    @field:Element(required = false)
    override var result: String? = null
}

class ApiResponseSimple : ApiResponseBase()

class ApiResponseCapability : ApiResponseBase() {

    @field:Element(required = false, name = "comm_proto_ver")
    var version: String? = null

    @field:Element(required = false, name = "productinfo")
    lateinit var info: ProductInfo

    class ProductInfo {
        @field:Element(required = false, name = "modelname")
        lateinit var modelName: String
    }
}
