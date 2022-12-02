import { render } from "@testing-library/react";
import React, { Component } from "react";
import { Redirect } from "react-router-dom";
import { Navigate } from 'react-router-dom';

import UserProfile from "./UserProfile"


export default class Login extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            name: '',
            username: '',
            password: '',
            passwordRepeated: '',
            city: '',
            country:'',
            loginError: null,
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
                    
                  console.log("yeah 400 or 403: " + response.status);
                }
                this.setState({
                    loginError:text
                });
                console.log("loginError set to: " +  this.state.loginError);

            } else {
                data = text && JSON.parse(text);
    
                if(data['token']){
                   localStorage.setItem("token", data['token']);
                   localStorage.setItem("userId", data['userId']);
                   this.setState( {['userProfile']: true});
                   console.log("Token present in response");                
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
        let status = null;

        console.log("in fucking submit");

        fetch('https://localhost:8080/auth/login', {
            method: 'POST',
            body: JSON.stringify({
                username: form.username.value,
                password: form.password.value,
             }),
            headers: {
                'Content-type': 'application/json; charset=UTF-8'
            },

        }).then(this.handleResponse);
    }
        
  render() {
    let error = this.state.loginError;

    return(
        <div className="Login-form-container">
            {this.state.userProfile &&(
                <div>
                    <Navigate to="/userProfile" replace={true} />
                </div>
            )}
        <form className="Login-form" onSubmit={this.handleSubmit}>
            <div className="Login-form-content">
            <h3 className="Login-form-title">Login</h3>
            <div className="form-group mt-3">
                <label>Email address</label>
                <input
                type="email"
                className="form-control mt-1"
                placeholder="Enter email"
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
                placeholder="Enter password"
                name="password"
                onChange={this.handleInputChange} 
                value={this.state.password}
                />
            </div>
            <div className="d-grid gap-2 mt-3">
                <button type="submit" className="btn btn-primary">
                Submit
                </button>
            </div>
            {error &&(
               <div>
                <p style={{ color: "red" }}>
                    {error}
                </p>
               </div>
            )}

            <p className="forgot-password text-right mt-2">
                Forgot <a href="#">password?</a>
            </p>
            </div>

           
        </form>
        </div>
    )
  }

   
}

