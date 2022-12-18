import React from 'react'
import { Navigate } from 'react-router-dom';
import { BrowserRouter, Routes, Route } from "react-router-dom"

export const Logout = () => {
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

        }).then((response) => {
            if (response.ok) {
                return response.json();
            } else
                throw new Error('Something went wrong');
          })
          .catch((error) => {
            console.log(error);
          });
      
          console.log("about to clear stuff");
        localStorage.clear();
       
    return <Navigate to="/login" replace={true} />; 
}

export default Logout;