import React from "react";
import { Navigate } from 'react-router-dom';

export default class EditQuote extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            username: '',
            city: '',
            country:'',
            profileError: [],
            returnToLogin: false,
            userProfile: false,
            quoteText: '',
            quoteAuthor: '',
            quoteId: '',
            quotePrivacySetting: ''
          };

          this.handleGetQuotesResponse = this.handleGetQuotesResponse.bind(this);
          this.handleDeleteQuoteResponse = this.handleDeleteQuoteResponse.bind(this);
          this.quotes = this.quotes.bind(this);
          this.updateQuote = this.updateQuote.bind(this);
          this.deleteQuote = this.deleteQuote.bind(this);
          this.handleInputChange = this.handleInputChange.bind(this);
     }

     componentDidMount() { 
        const token = localStorage['token'];
        const userId = localStorage['userId'];
        const quoteId = localStorage['quoteId'];
        console.log("Quote id: " + quoteId);

        if (token == null || userId == null || quoteId == null ) {
            this.setState({ returnToLogin: true});
        }

        let quoteUrl = "https://localhost:8080/quotes/" + userId + "/" + quoteId;
        console.log("URL: " + quoteUrl);
        fetch(quoteUrl, {
            method: 'GET',
            credentials: "include",
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Authorization': 'Bearer ' + token
            },

        }).then(this.handleGetQuotesResponse);
        console.log("retrieved quote details.");

        
     }

     handleGetQuotesResponse(response) {
        return response.text().then(text => {
            console.log("response from get quotes: " + text);

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
                   this.setState({ quoteAuthor: data['quoteAuthor'] });
                   this.setState({ quoteId: data['quoteId'] });  
                   this.setState({ quoteText: data['quoteText'] });  
                   this.setState({ quotePrivacySetting: data['quotePrivacySetting'] });    
                }
            }
        });
     }

     handleInputChange (e){
        const target = e.target;
        const name = target.name;
        const value = target.value;
        this.setState( {[name]: value});
    }

     updateQuote() {
        const token = localStorage['token'];
        const userId = localStorage['userId'];
        const quoteId = localStorage['quoteId'];
        if (token == null || userId == null ) {
            this.setState({ returnToLogin: true});
        }
        let quoteUrl = "https://localhost:8080/quotes";

        fetch(quoteUrl, {
            method: 'PUT',
            body: JSON.stringify({
                quoteId:quoteId,
                userId: userId,
                quoteAuthor: this.state.quoteAuthor,
                quoteText: this.state.quoteText,
                quotePrivacySetting: this.state.quotePrivacySetting
             }),
             credentials: "include",
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Authorization': 'Bearer ' + token
            },

        }).then(this.handleGetQuotesResponse).then(this.quotes)
     }

     deleteQuote(quoteIdToBeDeleted) {
        const token = localStorage['token'];
        const userId = localStorage['userId'];
        if (token == null || userId == null ) {
            this.setState({ returnToLogin: true});
        }
        
        console.log("Performing delete quote request...");
        console.log("Quote id: " + quoteIdToBeDeleted);

        const url = 'https://localhost:8080/quotes';
        fetch(url, {
            method: 'DELETE',
            body: JSON.stringify({
                quoteId:quoteIdToBeDeleted,
                userId: userId
             }),
             credentials: "include",
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Authorization': 'Bearer ' + token
            },
         }).then(this.quotes);
         console.log("Delete quote request concluded.");
     }

     handleDeleteQuoteResponse() {

     }

     quotes () {
        this.setState({ quotes: true});
     }

     render() {
        return(
            <div className="container">
                {this.state.returnToLogin &&(
                <div>
                    <Navigate to="/login" replace={true} />
                </div>
                 )}
                 {this.state.quotes &&(
                <div>
                    <Navigate to="/quotes" replace={true} />
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
                                <label for="fullName">Quote Text</label>
                                <input type="text" class="form-control" id="quoteText" name="quoteText"
                                    placeholder={this.state.quoteText}  onChange={this.handleInputChange} />
                            </div>
                        </div>
                        <div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
                            <div class="form-group">
                                <label for="quoteAuthor">Quote Author</label>
                                <input type="input" class="form-control" id="quoteAuthor" name="quoteAuthor"
                                    placeholder={this.state.quoteAuthor}  onChange={this.handleInputChange} />
                            </div>
                        </div>
                    </div>
                    <br/>
                    <div class="row gutters">
                        <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
                            <div class="text-right">
                                <button type="button" id="cancelEditQuote" name="cancelEditQuote" class="btn btn-secondary" onClick={this.quotes}>Cancel</button>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <button type="button" id="submit" name="submit" class="btn btn-primary" onClick={this.updateQuote}>Update</button>         
                            </div>
                            <div align="right">
                                <button type="button" id="deleteQuote" name="deleteQuote" class="btn btn-danger"  
                                    onClick={() => this.deleteQuote(this.state.quoteId)}>
                                    Delete</button>
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
