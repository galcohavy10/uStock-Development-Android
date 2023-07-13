//The rating is a new addition to posts
```
public class Post: Codable, Identifiable {
    public var id: String
    var caption: String
    var media: Media
    var user: String
    var rating: Int //added to provide number from 1-100 that evaluates user's progress
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
        case rating
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
}```
///
```public class User: Codable {
    var id: String
    var privacy: String
    var username: String
    var email: String?
    var phoneNumber: String?
    var firstName: String?
    var lastName: String?
    var dob: Date?
    var password: String? //apple and google users may not need a password anymore
    var appleUserToken: String? //apple
    var friends: [String]?
    var wallet: String?
    var investedUsers: [String]?
    var investedAspects: [String]?
    var stock: String?
    var competitions: [String]?
    var blockedUsers: [String]? //added to appease apple's mandatory blocking users feature
    
    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case privacy
        case username
        case email
        case phoneNumber
        case firstName
        case lastName
        case dob
        case password
        case appleUserToken
        case friends
        case wallet
        case investedUsers
        case investedAspects
        case stock
        case competitions
        case blockedUsers
    }```
