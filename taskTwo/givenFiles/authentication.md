//This will allow the login button to actually login

## Use existing users only at this point.

```    func logInUser(username: String, password: String, completion: @escaping (Bool) -> Void) {
        let body = ["username": username, "password": password] as [String : Any]
        makePostRequest(endpoint: "/api/login", body: body) { result in
            switch result {
            case .success(let data):
                do {
                    let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
                    print("JSON: \(String(describing: json))") // Debug print statement
                    if let userID = json?["userID"] as? String {
                        print("User ID is: \(userID)")
                        DispatchQueue.main.async {
                            //start function
                        }
                    } else {
                        DispatchQueue.main.async {
                            //handle error
                    }
                } catch let error as NSError {
                  //handle error
                    }
                }
            case .failure(let error):
                print(error)
                DispatchQueue.main.async {
                    //give user the result, error or success
                }
            }
        }
    }
```


// in practice, this'll be used with the "log in" button something like:

``` func login() {
            isLoading = true //show the user it's loading

            logInViewModel.logInUser(username: username, password: password) { success in
                if success {

                     //this next api call is made in order to allow user to move around the app with their id token

                    API.shared.user.getUserID(username: username) { result in
                        switch result {
                        case .success(let userID):
                            print("User ID is being sent to frontend: \(userID)")
                            do {
                                if let jsonData = userID.data(using: .utf8),
                                   let json = try JSONSerialization.jsonObject(with: jsonData, options: []) as? [String: String],
                                   let id = json["userID"] {
                                    print ("decoded ID is \(id)")

                                    UserPreferences.shared.userID = id //save it to the user's device so they can stay logged in if they want

                                } else {
                                    print("Error: could not extract userID from JSON")
                                }
                            } catch {
                                print("Error: \(error)")
                            }

                        case .failure(let error):
                          //handle error, the user needs to see what went wrong

                            print("Error: \(error.localizedDescription)")
                            let errorMessage = logInViewModel.errorMessage
                            DispatchQueue.main.async {
                                alertMessage = errorMessage ?? "no error"
                                showingAlert = true
                                self.loadingState.isLoading = false
                            }
                        }
                    }
                } else if let errorMessage = logInViewModel.errorMessage {
                    DispatchQueue.main.async {
                        alertMessage = errorMessage
                        showingAlert = true
                        self.loadingState.isLoading = false
                    }
                }
            }
    }
```


so here's the other api func you need, you need this to get the local ID so user can get their posts 

```
    public func getUserID(username: String, completion: @escaping (Result<String, Error>) -> Void) {
        let endpoint = "/api/getUserID"
        let body = ["username": username] as [String: Any]
        
        api?.makePostRequest(endpoint: endpoint, body: body) { result in
            switch result {
            case .success(let data):
                if let userID = String(data: data, encoding: .utf8) {
                    completion(.success(userID))
                } else {
                    completion(.failure(NSError(domain: "", code: -1, userInfo: [NSLocalizedDescriptionKey: "Invalid JSON"])))
                }
            case .failure(let error):
                completion(.failure(error))
            }
        }
    }
```

