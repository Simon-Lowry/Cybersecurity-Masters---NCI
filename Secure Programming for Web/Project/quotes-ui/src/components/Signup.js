import React from "react"
import { Navigate } from 'react-router-dom';


export default class Signup extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: '',
            username: '',
            password: '',
            passwordRepeated: '',
            city: '',
            country:'',
            signUpError: [],
            userProfile:''
          };
        
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleResponse = this.handleResponse.bind(this);

     }

     componentDidMount() { 
        localStorage.clear();
        
     }

     handleResponse(response) {
        return response.text().then(text => {
            console.log("token: " + text);
           
            let data = null;
            if (!response.ok) {
                if ([401, 400].includes(response.status)) {
                  localStorage.removeItem("token", data['token']);              
                  console.log("yeah 400 or 403: " + response.status);
                }
    
                const error = (data && data.message) || response.statusText;
                console.log("Error: " + error);
                this.setState({['signUpError']:error})
                return error;
            } else {
                data = text && JSON.parse(text);
    
                if(data['token']){
                   localStorage.setItem("token", data['token']);
                   localStorage.setItem("userId", data['userId']);
                   this.setState( {['userProfile']: true});
                   console.log("Token present in response");
                   return data;
                
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

      handleSubmit(event) {
        event.preventDefault();
        const form = event.target;

        fetch('https://localhost:8080/auth/signUp', {
            method: 'POST',
            body: JSON.stringify({
                name:form.name.value,
                username: form.username.value,
                password: form.password.value,
                passwordRepeated: form.passwordRepeated.value,
                city: form.city.value,
                country: form.country.value
             }),
            headers: {
                'Content-type': 'application/json; charset=UTF-8'
            },
         })
         .then(res => res.json())
         .then(res => {
            console.log("Response: " + res);
            // TODO: handle response, currently only expecting error case
            this.setState({['signUpError']:res});
         })
        console.log('Signup form submitted');
    };
  render() {
    return(
        <div className="Signup-form-container">
            {this.state.userProfile &&(
                <div>
                    <Navigate to="/userProfile" replace={true} />
                </div>
            )}
        <form className="Signup-form" onSubmit={this.handleSubmit}>
          <div className="Signup-form-content">
            <h3 className="Signup-form-title">Signup</h3> 
            {this.state.signUpError &&(
               <div>
                <ul style={{ color: "red" }}>
                    {this.state.signUpError.map((item, i) => {
                        return <li key={i}>{item}</li>
                    })}
                </ul>
               </div>
            )}
            <div className="form-group mt-3">
              <label>Full Name</label>
              <input
                type="input"
                className="form-control mt-1"
                placeholder="e.g Jane Doe"
                name="name"
                onChange={this.handleInputChange} 
                value={this.state.name}
              />
            </div>
            <div className="form-group mt-3">
              <label>Email address</label>
              <input
                type="email"
                className="form-control mt-1"
                placeholder="Email Address"
                name="username"
                onChange={this.handleInputChange} 
                value={this.state.username}
              />
            </div>
            <div className="form-group mt-3">
              <label>Password</label>
              <input
                type="password"
                className="form-control mt-1"
                placeholder="Password"
                name="password"
                onChange={this.handleInputChange} 
                value={this.state.password}
              />
            </div>
            <div className="form-group mt-3">
              <label>Password Repeated</label>
              <input
                type="password"
                className="form-control mt-1"
                placeholder="Password"
                name="passwordRepeated"
                onChange={this.handleInputChange} 
                value={this.state.passwordRepeated}
              />
            </div>
            <div className="form-group mt-3">
              <label>City</label>
              <input
                type="input"
                className="form-control mt-1"
                placeholder="City"
                name="city"
                onChange={this.handleInputChange} 
                value={this.state.city}
              />
            </div>
            <div className="form-group mt-3">
              <label>Country</label>
              <input
                type="input"
                className="form-control mt-1"
                placeholder="Country"
                name="country"
                onChange={this.handleInputChange} 
                value={this.state.country}
              />
            </div>
            <div className="d-grid gap-2 mt-3">
              <button type="submit" className="btn btn-primary">
                Submit
              </button>
            </div>
          </div>
        </form>
      </div>
    )
  }
}
