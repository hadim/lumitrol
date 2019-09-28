package org.hadim.lumitrol.model.api

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name="camrply", strict = false)
class ApiResponse {
    @field:Element(required = false) var result: String? = null
    @field:Element(required = false) var state: ApiResponseState? = null
}

@Root(name="state", strict = false)
class ApiResponseState {
    // TODO: Implement here.
    //  Maybe we need to parse in a dynamic manner since
    //  all cameras might not have the same fields.
}
