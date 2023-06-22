// USE GPT-4 if you can! It can really help you understand what this data structure is doing if you don't understand.

// "?" marks are for optional values so the compiler knows this value might be nil.

// CodingKeys are used when the name of the element from the server is not the same as the element named on the phone. _id and id are different, for example.



public class Post: Codable {
    var id: String
    var caption: String
    var media: Media
    var user: String
    var aspects: [String]?
    var upvotes: [String]?
    var downvotes: [String]?
    var createdAt: Date
    var updatedAt: Date?
    var comments: [String]?
    var tags: [String]?
    var mentions: [String]?
    
    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case caption
        case media
        case user
        case aspects
        case upvotes
        case downvotes
        case createdAt
        case updatedAt
        case comments
        case tags
        case mentions
    }
}

public class Media: Codable {
    var type: String
    var content: String?
    var url: String?
    
    enum CodingKeys: String, CodingKey {
        case type
        case content
        case url
    }
}

