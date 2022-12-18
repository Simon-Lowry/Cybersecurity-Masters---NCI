import React from 'react'
import { Navigate } from 'react-router-dom';
import { BrowserRouter, Routes, Route } from "react-router-dom"

export const ProtectedRoute = ({ children }) => {
    const Component = children.Component;
    const isAuthenticated = localStorage.getItem('token');
       
    return isAuthenticated ? (
            children
    ) : (
            <Navigate to="/login" replace={true} />
    );
    
}

export default ProtectedRoute;