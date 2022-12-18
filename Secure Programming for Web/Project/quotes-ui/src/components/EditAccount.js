import React from "react";
import { Navigate } from 'react-router-dom';

export default class EditAccount extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            username: '',
            password: '',
            city: '',
            country:'',
            profileError: [],
            returnToLogin: false,
            userProfile: false,
          };

          this.handlePageLoadUserGetResponse = this.handlePageLoadUserGetResponse.bind(this);
          this.handleUpdateAccountResponse = this.handleUpdateAccountResponse.bind(this);
          this.handleDeleteAccountResponse = this.handleDeleteAccountResponse.bind(this);
          this.userProfile = this.userProfile.bind(this);
          this.updateAccount = this.updateAccount.bind(this);
          this.deleteAccount = this.deleteAccount.bind(this);
          this.handleInputChange = this.handleInputChange.bind(this);

     }

     handleInputChange (e){
        const target = e.target;
        const name = target.name;
        const value = target.value;
        this.setState( {[name]: value});
    }

     componentDidMount() { 
        const token = localStorage['token'];
        const userId = localStorage['userId'];
        if (token == null || userId == null ) {
            this.setState({ returnToLogin: true});
        }

        let userUrl = "https://localhost:8080/users/" + userId;
        console.log("URL: " + userUrl);
        console.log("token before get: " + token);
        fetch(userUrl, {
            method: 'GET',
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Authorization': 'Bearer ' + token
            },
            credentials: "include",

        }).then(this.handlePageLoadUserGetResponse);
        
     }

     handlePageLoadUserGetResponse(response) {
        return response.text().then(text => {
            console.log("response from get user: " + text);

            let data = null;
            if (!response.ok) {
                if ([401, 400].includes(response.status)) {
                  console.log("yeah 400 or 401: " + response);
                }
                this.setState({
                    returnToLogin: true
                }); 
            } else {
                data = text && JSON.parse(text);
    
                if(data != null){
                   this.setState({ name: data['name'] });
                   this.setState({ username: data['email'] });  
                   this.setState({ city: data['city'] });  
                   this.setState({ country: data['country'] });    
                }
            }
        });
     }

     userProfile() {
        this.setState({ userProfile: true});
     }

     updateAccount() {
        const token = localStorage['token'];
        const userId = localStorage['userId'];
        if (token == null || userId == null ) {
            this.setState({ returnToLogin: true});
        }
    
        console.log("country: " + this.state.country);
        console.log("user id: " + userId);
        console.log("username: " + this.state.username);
        console.log("city: " + this.state.city);
        console.log("name: " + this.state.name);

        let userUrl = "https://localhost:8080/users/" + userId;
        console.log("URL: " + userUrl);
        console.log("token before get: " + token);
        fetch(userUrl, {
            method: 'PUT',
            body: JSON.stringify({
                username: this.state.username,
                country: this.state.country,
                city: this.state.city,
                name: this.state.name,
                userId: userId
             }),
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Authorization': 'Bearer ' + token
            },
            credentials: "include",

        }).then(this.handleUpdateAccountResponse);
        
     }

     deleteAccount() {
        const token = localStorage['token'];
        const userId = localStorage['userId'];
        if (token == null || userId == null ) {
            this.setState({ returnToLogin: true});
        }
        
        let userUrl = "https://localhost:8080/users/" + userId;
        console.log("URL: " + userUrl);
        console.log("Password: " + this.state.password);
        console.log("user id: " + userId);
        console.log("username: " + this.state.username);

        fetch(userUrl, {
            method: 'DELETE',
            body: JSON.stringify({
                userId: userId,
                password: this.state.password,
                username: this.state.username,
             }),
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
             //   'Authorization': 'Bearer ' + token
            },
            credentials: "include",

        }).then(this.handlePageLoadUserGetResponse);
        

     }


     handleUpdateAccountResponse(response) {
        return response.text().then(text => {
            console.log("response from get user: " + text);

            let data = null;
            if (!response.ok) {
                if ([401, 400].includes(response.status)) {
                  console.log("yeah 400 or 401: " + response);
                }
                this.setState({
                    returnToLogin: true
                }); 
            } else {
                data = text && JSON.parse(text);
    
                if(data != null){
                   this.setState({ name: data['name'] });
                   this.setState({ username: data['email'] });  
                   this.setState({ city: data['city'] });  
                   this.setState({ country: data['country'] });    
                }
            }
        });
     }

     handleDeleteAccountResponse(response) {
        return response.text().then(text => {
            console.log("response from get user: " + text);

            if (!response.ok) {
                if ([401, 400].includes(response.status)) {
                  console.log("yeah 400 or 401: " + response);
                }
                this.setState({
                    returnToLogin: true
                }); 
            } else {
                this.setState({
                    returnToLogin: true
                }); 
            }
        });

     }

     render() {
        return(
            <div className="container">
                {this.state.returnToLogin &&(
                <div>
                    <Navigate to="/login" replace={true} />
                </div>
                 )}
                  {this.state.userProfile &&(
                <div>
                    <Navigate to="/userProfile" replace={true} />
                </div>
                 )}

            <div class="container">
            <div class="row gutters">
            <div class="col-xl-3 col-lg-3 col-md-12 col-sm-12 col-12">
            <div class="card h-100">
                <div class="card-body">
                    <div class="account-settings">
                        <div class="user-profile">
                            <div class="user-avatar">
                                <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="Maxwell Admin"/>
                            </div>
                            <h5 class="user-name">{this.state.name}</h5>
                            <h6 class="user-email">{this.state.username}</h6>
                        </div>
                        <div class="about">
                            <h5>About</h5>
                            <p>Full Stack Designer I enjoy creating user-centric, delightful and human experiences.</p>
                        </div>
                    </div>
                </div>
            </div>
            </div>
            <div class="col-xl-9 col-lg-9 col-md-12 col-sm-12 col-12">

            <div class="card h-100">
                <div class="card-body">
                    <div class="row gutters">
                        <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                            <h6 class="mb-2 text-primary">Personal Details</h6>
                        </div>
                        <div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
                            <div class="form-group">
                                <label for="fullName">Full Name</label>
                                <input type="text" name="name" class="form-control" id="fullName" onChange={this.handleInputChange} 
                                placeholder={this.state.name}/>
                            </div>
                        </div>
                        <div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
                            <div class="form-group">
                                <label for="eMail">Email</label>
                                <input type="email" name="username" class="form-control" id="eMail" onChange={this.handleInputChange} 
                                placeholder={this.state.username}/>
                            </div>
                        </div>
                    </div>
                    <div class="row gutters">
                        <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                            <h6 class="mt-3 mb-2 text-primary">Address</h6>
                        </div>
                        <div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
                            <div class="form-group">
                                <label for="Country">Country</label>
                                <input type="name" name="country" class="form-control" id="Country" onChange={this.handleInputChange} 
                                placeholder={this.state.country}/>
                            </div>
                        </div>
                        <div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
                            <div class="form-group">
                                <label for="ciTy">City</label>
                                <input type="name" name="city" class="form-control" id="city" onChange={this.handleInputChange} 
                                placeholder={this.state.city}/>
                            </div>
                        </div>
                        
                    </div>
                    <br/>
                    <div class="row gutters">
                        <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                            <div class="text-right">
                                <button type="button" id="submit" name="submit" class="btn btn-secondary" onClick={this.userProfile}>Cancel</button>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <button type="button" id="submit" name="submit" class="btn btn-primary" onClick={this.updateAccount}>Update</button>         
                            </div>
                      
                            <div align="right">
                                <div class="col-xl-4 col-lg-4 col-md-4 col-sm-4 col-4">
                                    <div class="form-group">
                                        <label for="password">Password</label>
                                        <input type="password" name="password" class="form-control" id="password"  onChange={this.handleInputChange} 
                                        placeholder={this.state.password}/>
                                    </div>
                                </div>
                                <br/>
                                <button type="button" id="submit" name="submit" class="btn btn-danger" onClick={this.deleteAccount}>Delete Account</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </div>
            </div>
            </div>
            </div>

            
        )
    }

}