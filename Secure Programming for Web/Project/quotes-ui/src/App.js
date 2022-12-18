import "bootstrap/dist/css/bootstrap.min.css"
import "./App.css"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import Login from "./components/Login"
import Signup from "./components/Signup"
import UserProfile from "./components/UserProfile"
import Quotes from "./components/Quotes"
import EditQuote from "./components/EditQuote"
import EditAccount from "./components/EditAccount"
import React from 'react';
import ProtectedRoute from "./ProtectedRoute"
import { Navigate } from 'react-router-dom';



function App() {
  return (
    <BrowserRouter>
    
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signUp" element={<Signup />} />

        <Route path="/userProfile" element={
          <ProtectedRoute>
            <UserProfile />
          </ProtectedRoute>
        } />
        <Route path="/quotes"  element={
          <ProtectedRoute>
            <Quotes />
          </ProtectedRoute>
        } />
        <Route path="/editQuote" element={
          <ProtectedRoute>
            <EditQuote />
          </ProtectedRoute>
        } />
        <Route path="/editAccount" element={
          <ProtectedRoute>
            <EditAccount />
          </ProtectedRoute>
        } />

      <Route path="/" element={<Login />} />

      </Routes>
    </BrowserRouter>

  )
}

export default App