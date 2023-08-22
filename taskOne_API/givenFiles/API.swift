// YOUR TWO MAIN CHALLENGES: sending AND receiving data. you need to parse the JSON response correctly.

// Use print values to debug responses and, I'll say it again, debugging with GPT-4 is god mode.

// TWO MAIN HTTP REQUEST TYPES: Don't worry about anything but post requests for now. 



public let server = "https://enigmatic-plateau-21257.herokuapp.com"


//this class is used to make requests to the server
public class API {
    
    
    func makeGetRequest(endpoint: String, completion: @escaping (Result<Data, Error>) -> Void) {
        let url = URL(string: server + endpoint)!
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("application/json", forHTTPHeaderField: "Accept")
        
        
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data, error == nil else {
                completion(.failure(error!))
                return
            }
            completion(.success(data))
        }
        task.resume()
    }
    
    
    func makePostRequest(endpoint: String, body: [String: Any], completion: @escaping (Result<Data, Error>) -> Void) { 
        let url = URL(string: server + endpoint)!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("application/json", forHTTPHeaderField: "Accept")
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)
        
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data, error == nil else {
                completion(.failure(error!))
                return
            }
            completion(.success(data))
        }
        task.resume()
    }
    
  //You are requesting an array of posts using the userID in the request body.
  
      public func getMyPosts(userID: String, completion: @escaping (Result<([Post]), Error>) -> Void) {
        let endpoint = "/api/getMyPosts"
        let body = ["userID": userID]
        
        makePostRequest(endpoint: endpoint, body: body) { result in
            switch result {
            case .success(let data):
                print(String(data: data, encoding: .utf8) ?? "Cannot convert data to string")
                do {
                    print("Received data: \(String(data: data, encoding: .utf8) ?? "Unable to convert data to string")")
                    
                    if let jsonResponse = try JSONSerialization.jsonObject(with: data, options: []) as? [[String: Any]] {
                        var posts = [Post]()
                        let decoder = JSONDecoder()
                        
                        
                        // This block of code is for decoding the date, there is a discrepany between how the date is being stored on database and how it's being displayed. 
                        // Only build this if you find that kotlin apps have a similar discrepancy
                        let iso8601Formatter = ISO8601DateFormatter()
                        iso8601Formatter.formatOptions = [.withInternetDateTime, .withFractionalSeconds]
                        decoder.dateDecodingStrategy = .custom { decoder -> Date in
                            let container = try decoder.singleValueContainer()
                            let dateString = try container.decode(String.self)
                            
                            if let date = iso8601Formatter.date(from: dateString) {
                                return date
                            } else {
                                throw DecodingError.dataCorruptedError(in: container, debugDescription: "Date string does not match expected format.")
                            }
                        }
                        
                        for item in jsonResponse {
                            let postsData = try JSONSerialization.data(withJSONObject: item, options: [])
                                let post = try decoder.decode(Post.self, from: postsData)
                                posts.append(post)
                        }
                        
                        print("Decoded posts: \(posts)")
                        
                        completion(.success((posts)))
                    } else {
                        completion(.failure(NSError(domain: "", code: -1, userInfo: [NSLocalizedDescriptionKey: "Invalid JSON structure"])))
                    }
                } catch {
                    print("Error decoding your posts: \(error)")
                    print("Error decoding your posts localized description: \(error.localizedDescription)")
                    completion(.failure(error))
                }
            case .failure(let error):
                print("Error fetching your posts: \(error.localizedDescription)")
                completion(.failure(error))
            }
        }
    }
    
 }
