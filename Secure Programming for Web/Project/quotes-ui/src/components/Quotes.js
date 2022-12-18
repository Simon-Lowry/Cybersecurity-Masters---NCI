import React from "react";
import { Navigate } from 'react-router-dom';
import Logout from "./Logout";


export default class Quotes extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            quotes: [],
            quotesPageReload: false,
            quoteText: '',
            quoteId: '',
            quotePrivacy: '',
            quoteAuthor:'',
            quotesError: [],
            returnToLogin: false,
            editQuote: false,
            error: '',
            createQuoteError: '',
            stopGetQuotes: false,
            editAccount: false,
            logout: false
          };

          this.quotesPageReloader = this.quotesPageReloader.bind(this);
          this.handleGetQuotesResponse = this.handleGetQuotesResponse.bind(this);
          this.handleCreateQuoteResponse = this.handleCreateQuoteResponse.bind(this);
          this.getAllQuotesForUser = this.getAllQuotesForUser.bind(this);
          this.handleInputChange = this.handleInputChange.bind(this);
          this.handleDeleteQuoteResponse = this.handleDeleteQuoteResponse.bind(this);
          this.logout = this.logout.bind(this);
          this.editQuote = this.editQuote.bind(this);
          this.editAccount = this.editAccount.bind(this);
       //   this.handleLogoutResponse = this.handleLogoutResponse.bind(this);

     }

     componentDidMount() { 
        this.getAllQuotesForUser();
     }

     handleInputChange (e){
        const target = e.target;
        const name = target.name;
        const value = target.value;
        this.setState( {[name]: value});
    }

     getAllQuotesForUser() {
        if (this.state.stopGetQuotes == true) {
            return;
        }

        if (this.state.quotes.length != 0 && this.state.stopGetQuotes == false) {
            this.setState({ stopGetQuotes: true });
            return;
        } 
        const token = localStorage['token'];
        const userId = localStorage['userId'];
        if (token == null || userId == null ) {
            this.setState({ returnToLogin: true});
        }
        let quoteUrl = "https://localhost:8080/quotes/getAllQuotes/" + userId;
        console.log("URL: " + quoteUrl);
        fetch(quoteUrl, {
            method: 'GET',
            credentials: "include",
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Authorization': 'Bearer ' + token
            },

        }).then(this.handleGetQuotesResponse);
     }

     handleGetQuotesResponse(response) {
        return response.text().then(text => {
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
                    console.log("Data in response: " + data);

                    for(let i = 0; i < data.length; i++) {
                        let obj = data[i];
                    
                        this.state.quotes.push(obj);
                    }

                    for(let i = 0; i < this.state.quotes.length; i++) {
                        let obj = this.state.quotes[i];                    
                    }
                    this.setState({ quotes: this.state.quotes});
                }
            }
        });
     }

     logout() {
        const token = localStorage['token'];

        let userUrl = "https://localhost:8080/auth/logout";
        console.log("URL: " + userUrl);
        fetch(userUrl, {
            method: 'POST',
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Authorization': 'Bearer ' + token
            },
            credentials: "include",
        });
      
        localStorage.clear();
        this.setState({ logout: true});
     }

     editQuote() {
        this.setState({ editQuote: true});
     }

      handleSubmit(event) {
        const token = localStorage['token'];
        const userId = localStorage['userId'];
        if (token == null || userId == null ) {
            this.setState({ returnToLogin: true});
        }
        event.preventDefault();
        const form = event.target;

        console.log("Performing create quote request...");
        const url = 'https://localhost:8080/quotes';
        fetch(url, {
            method: 'POST',
            body: JSON.stringify({
                quoteText:form.quoteText.value,
                quoteAuthor: form.quoteAuthor.value,
                quotePrivacySetting: "PRIVATE",
                userId: userId
             }),
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Authorization': 'Bearer ' + token
            },
            credentials: "include",
         })
         //.then(this.handleCreateQuoteResponse);
         window.location.reload(false);


    };

    handleCreateQuoteResponse(response) {
        console.log("Response:  " + response);
        return response.text().then(text => {
            console.log("response from create quotes: " + text);

            let data = null;
            if (!response.ok) {
                if ([401, 400].includes(response.status)) {
                  console.log("yeah 400 or 401: " + response);
                }
                window.location.reload(false);
                this.setState({ createQuoteError: response });
            } else {
                data = text && JSON.parse(text);
    
                if(data != null){
                    console.log("Data in response: " + data);
                }
            }
        });

    }

    updateQuote(quoteId) {
        console.log("Edit quote called...");
        console.log("Quote id: " + quoteId);
        localStorage.setItem("quoteId", quoteId);
        this.setState({ editQuote: true });
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
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Authorization': 'Bearer ' + token
            },
         }).then(this.handleDeleteQuoteResponse);
         console.log("Delete quote request concluded.");
         this.getAllQuotesForUser();

    }

    handleDeleteQuoteResponse() {

    }



    quotesPageReloader(response) {
        this.setState({ quotesPageReload: true});
    }

    editAccount() {
        this.setState({
            editAccount: true
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
                 {this.state.editQuote &&(
                <div>
                    <Navigate to="/editQuote" replace={true} />
                </div>
                 )}
                 {this.state.quotesPageReload &&(
                <div>
                    <Navigate to="/quotes" replace={true} />
                </div>
                 )}
                 {this.state.editAccount &&(
                <div>
                    <Navigate to="/editAccount" replace={true} />
                </div>
                 )}
                  {this.state.logout &&(
                <div>
                    <Navigate to="/login" replace={true} />
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
                        <form className="Login-form" onSubmit={this.handleSubmit}>
                <div className="Login-form-content">
                <h5 className="Login-form-title">Create new Quote</h5>
                <div className="form-group mt-3">
                    <label>Quote Text</label>
                    <input
                    type="textbox"
                    className="form-control mt-1"
                    placeholder="Enter quote here"
                    name="quoteText"
                    onChange={this.handleInputChange} 
                    value={this.state.quoteText}
                    />
                </div>
                <div className="form-group mt-3">
                    <label>Author:</label>
                    <input
                    type="input"
                    className="form-control mt-1"
                    placeholder="Enter Author's name"
                    name="quoteAuthor"
                    onChange={this.handleInputChange} 
                    value={this.state.quoteAuthor}
                    />
                </div>
                <div className="d-grid gap-2 mt-3">
                    <button type="submit" className="btn btn-primary">
                    Create Quote
                    </button>
                </div>
                {this.state.error &&(
                <div>
                    <p style={{ color: "red" }}>
                        {this.state.error}
                    </p>
                </div>
                )}

                </div>

           
            </form>
                    </div>
                    <div className="col-lg-8">
                        <button onClick={this.editAccount}align="left">
                                Edit Account
                            </button>
                            <button onClick={this.quotes} align="left">
                                Quotes
                            </button>
                            <button onClick={this.logout} align="right">
                                Logout
                            </button>
                        <br/><br/>  <br/>


                        <div className="row">
                            <br/>
                            <h2>Quotes</h2>
                            <br/>
                           
                            {this.state.quotes.map(quote => {
                                return (
                                <div key={quote.id}>
                                    <p><em>{quote.quoteText}</em></p>
                                    <b>Author:</b> &nbsp; {quote.quoteAuthor}
                                    <div align="right">
                                        <button type="submit" id="updateQuote" name="updateQuote" class="btn btn-primary btn-sm" 
                                            onClick={() => this.updateQuote(quote.id)}> Edit Quote</button> 
                                    </div>
                                    <hr />
                                </div>
                                );
                            })}
                        </div>
                    </div>
                  
                 </div>

            </div>

            )
    }


}
