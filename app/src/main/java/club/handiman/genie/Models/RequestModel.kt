package club.handiman.genie.Models

import org.json.JSONArray

class RequestModel(
    var request_id: String,
    var handyman: String?,
    var image: String,
    var request: String,
    var has_receipt: Boolean,
    var has_paid: Boolean,
    //var created_at_date: String,
    var date: String,
    var from: String,
    var to: String,
    var subject: String,
    var images: JSONArray,
    var receipt: JSONArray? = null,
    var receipt_images: JSONArray? = null,
    var total: Any? = null,
    var rescheduled: Boolean? = null,
    var rescheduled_from: String? = null,
    var rescheduled_to: String? = null,
    var rescheduled_date: String? = null
//    var request_title: String,
//    var description: String,
//    var street: String,
//    var state: String,
//    var longitude: Double,
//    var latitude: Double,
//    var request_from: String,
//    var request_to: String,
//    var service_name:String
) {

}