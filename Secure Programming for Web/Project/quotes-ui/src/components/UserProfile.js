import React from "react";
import { Navigate } from 'react-router-dom';

export default class UserProfile extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            username: '',
            city: '',
            country:'',
            profileError: [],
            returnToLogin: false,
            editAccount: false,
            quotes: false
          };

          this.handlePageLoadUserGetResponse = this.handlePageLoadUserGetResponse.bind(this);
          this.logout = this.logout.bind(this);
          this.quotes = this.quotes.bind(this);
          this.editAccount = this.editAccount.bind(this);
          this.handleLogoutResponse = this.handleLogoutResponse.bind(this);

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
                    console.log("data: " + data['country']);
                   this.setState({ name: data['name'] });
                   this.setState({ username: data['email'] });  
                   console.log("username: " + this.state.email);
                   this.setState({ city: data['city'] });  
                   this.setState({ country: data['country'] });    
                }
            }
        });
     }

     quotes() {
        this.setState({
            quotes: true
        }); 
     }

     editAccount() {
        this.setState({
            editAccount: true
        }); 
     }

     logout() {
        localStorage.clear();

        // TODO: call logout on backend and adjust this code
        /*
        let userUrl = "https://localhost:8080/auth/logout";
        console.log("URL: " + userUrl);
        fetch(userUrl, {
            method: 'GET',
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Authorization': 'Bearer ' + token
            },

        }).then(this.handleLogoutResponse);
        */
      
        this.setState({
            returnToLogin: true
        }); 
     }

     handleLogoutResponse() {

     }

     render() {
        return(
            <div className="container">
                {this.state.returnToLogin &&(
                <div>
                    <Navigate to="/login" replace={true} />
                </div>
                 )}
                  {this.state.editAccount &&(
                <div>
                    <Navigate to="/editAccount" replace={true} />
                </div>
                 )}
                  {this.state.quotes &&(
                <div>
                    <Navigate to="/quotes" replace={true} />
                </div>
                 )}
                <div className="row">
                    <div className="col-lg-4">
                        <div className="profile-card-4 z-depth-3">
                            <div className="card">
                            <div className="card-body text-center bg-primary rounded-top">
                                <div className="user-box">
                                    <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="user avatar"/>
                                </div>
                                <h5 className="mb-1 text-white">{this.state.name}</h5>
                                <h6 className="text-light">Quoteaholic</h6>
                                </div>
                                <div className="card-body">
                                    <ul className="list-group shadow-none">
                                    <li className="list-group-item">
                                        <div>
                                            Email Address: {this.state.username}
                                        </div>
                                    </li>
                                    <li className="list-group-item">
                            
                                        <div>
                                        City: {this.state.city}
                                        </div>
                                    </li>
                                    <li className="list-group-item">
                                        
                                        <div>
                                        Country: {this.state.country}
                                        </div>

                                    </li>
                                    </ul>
                                    <div className="row text-center mt-4">
                                    <div className="col p-2">
                                    <h4 className="mb-1 line-height-5">154</h4>
                                        <small className="mb-0 font-weight-bold">Quotes</small>
                                    </div>
                                        <div className="col p-2">
                                        <h4 className="mb-1 line-height-5">2.2k</h4>
                                        <small className="mb-0 font-weight-bold">Connections</small>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-lg-4">
                        <button onClick={this.editAccount}>
                            Edit Account
                        </button>
                        <button onClick={this.quotes}>
                            Quotes
                        </button>
                     </div>
                     <div className="col-lg-4" >
                        <button onClick={this.logout}>
                            Logout
                        </button>
                     </div>
                  
                 </div>
                 
            </div>
            
        );
     }


}