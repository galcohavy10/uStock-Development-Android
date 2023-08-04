### Post Retrieval


```
//when the post appears you want to fetch data if needed
//the keyword is do this when a post appears, you don't want to overload the phone.

//Additionally, don't worry about the different loading views make one simple loading overlay for all of them and it should work for now.

        private func loadContentIfNeeded() {
            guard let mediaUrl = post.media.url else {
                return
            }

              
          //IGNORE THE CACHING PROTOCOLS. you don't need to implement this yet so just pass the post's ID directly to the api call regardless.

            if post.media.type == "image" && postImage == nil {
                if let imageData = cachedMedia.get(forKey: mediaUrl) {
                    self.postImage = UIImage(data: imageData) 
                    print ("used a cached image instead")
                } else {
                    imageLoading = true 
                    API.shared.fetchImageData(postID: post.id) { result in
                        DispatchQueue.main.async {
                            self.imageLoading = false
                            switch result {
                            case .success(let image):
                                if let imageData = image.jpegData(compressionQuality: 1.0) {
                                    self.cachedMedia.insert(imageData, forKey: mediaUrl)
                                    print ("inserted image to cache")
                                }
                                self.postImage = image
                            case .failure(let error):
                                print("Failed to fetch image data: \(error)")
                            }
                        }
                    }
                }
            } else if post.media.type == "video" && postVideoURL == nil {
                if let url = cachedMedia.getVideo(forKey: mediaUrl) {
                    self.postVideoURL = url
                    print ("used a cached video instead")
                } else {
                    videoLoading = true
                    API.shared.fetchVideoURL(postID: post.id) { result in
                        DispatchQueue.main.async {
                            self.videoLoading = false
                            switch result {
                            case .success(let url):
                                self.cachedMedia.insertVideo(from: url, forKey: mediaUrl)
                                self.postVideoURL = url
                                print ("inserted video to cache")
                            case .failure(let error):
                                print("Failed to fetch video URL: \(error)")
                            }
                        }
                    }
                }
            } else if post.media.type == "audio" && postAudioURL == nil {
                if let url = cachedMedia.getVideo(forKey: mediaUrl) {
                    self.postAudioURL = url
                    print ("used a cached audio instead")
                } else {
                    audioLoading = true
                    API.shared.fetchAudioURL(postID: post.id) { result in
                        DispatchQueue.main.async {
                            switch result {
                            case .success(let url):
                                self.cachedMedia.insertVideo(from: url, forKey: mediaUrl)
                                self.postAudioURL = url
                                print("url: \(url)")
                                self.audioLoading = false
                                print ("inserted audio to cache")
                            case .failure(let error):
                                print("Failed to fetch audio URL: \(error)")
                            }
                        }
                    }
                }
            }
        }

```
