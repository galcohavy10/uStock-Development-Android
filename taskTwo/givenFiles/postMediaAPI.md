### You need to get the post media for image, video, and audio


##### Video and Audio are using URL but Image is actually getting the whole file


```
///get any image, this is getting the whole file. the response will look like base64 data.
    public func fetchImageData(postID: String, completion: @escaping (Result<UIImage, Error>) -> Void) {
        let endpoint = "/api/fetchImageData"
        let parameters: [String: Any] = ["postID": postID]

        makePostRequest(endpoint: endpoint, body: parameters) { result in
            switch result {
            case .success(let data):
                do {
                    print(data)
                    if let jsonResponse = try JSONSerialization.jsonObject(with: data, options: []) as? [String: String],

                        //notice the key of the response is base64Image make sure to correspond with this.

                       let base64Image = jsonResponse["base64Image"] { 
                        guard let imageData = Data(base64Encoded: base64Image, options: .ignoreUnknownCharacters),
                              let image = UIImage(data: imageData) else {
                            throw NSError(domain: "", code: -2, userInfo: [NSLocalizedDescriptionKey: "Base64 decoding failed or could not create image"])
                        }
                        completion(.success(image))
                    } else {
                        throw NSError(domain: "", code: -3, userInfo: [NSLocalizedDescriptionKey: "Expected JSON structure not found"])
                    }
                } catch {
                    print("Error fetching image data on Post View: \(error)")
                    completion(.failure(error))
                }
            case .failure(let error):
                print("Error fetching image data: \(error.localizedDescription)")
                completion(.failure(error))
            }
        }
    }
    
    //get any video using authorized URL
    public func fetchVideoURL(postID: String, completion: @escaping (Result<URL, Error>) -> Void) {
        let endpoint = "/api/fetchVideoAuthorizedURL"
        let parameters: [String: Any] = ["postID": postID]
        
        makePostRequest(endpoint: endpoint, body: parameters) { result in
            switch result {
            case .success(let data):
                do {
                    if let jsonResponse = try JSONSerialization.jsonObject(with: data, options: []) as? [String: String],
                       let urlString = jsonResponse["url"],
                       let url = URL(string: urlString) {
                        completion(.success(url))
                    } else {
                        throw NSError(domain: "", code: -3, userInfo: [NSLocalizedDescriptionKey: "Expected JSON structure not found"])
                    }
                } catch {
                    print("Error fetching video URL: \(error)")
                    completion(.failure(error))
                }
            case .failure(let error):
                print("Error fetching video URL: \(error.localizedDescription)")
                completion(.failure(error))
            }
        }
    }
    
    //get any audio using authorized URL
    public func fetchAudioURL(postID: String, completion: @escaping (Result<URL, Error>) -> Void) {
        let endpoint = "/api/fetchAudioAuthorizedURL"
        let parameters: [String: Any] = ["postID": postID]
        
        makePostRequest(endpoint: endpoint, body: parameters) { result in
            switch result {
            case .success(let data):
                do {
                    if let jsonResponse = try JSONSerialization.jsonObject(with: data, options: []) as? [String: String],
                       let urlString = jsonResponse["url"],
                       let url = URL(string: urlString) {
                        completion(.success(url))
                    } else {
                        throw NSError(domain: "", code: -3, userInfo: [NSLocalizedDescriptionKey: "Expected JSON structure not found"])
                    }
                } catch {
                    print("Error fetching audio URL: \(error)")
                    completion(.failure(error))
                }
            case .failure(let error):
                print("Error fetching audio URL: \(error.localizedDescription)")
                completion(.failure(error))
            }
        }
    }
```
