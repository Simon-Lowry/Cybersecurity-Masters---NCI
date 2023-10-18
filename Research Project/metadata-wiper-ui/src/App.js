import React, { Component } from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom"
import JPEG from "./Components/JPEG"
import PDF from "./Components/PDF"
import Excel from "./Components/Excel"
import DOCX from "./Components/DOCX"
import Home from "./Components/Home"
import 'bootstrap/dist/css/bootstrap.min.css';
import {Nav,  Navbar, NavDropdown } from 'react-bootstrap';


function App() {
  return (
        <div>
            <Navbar collapseOnSelect expand="lg" bg="primary" variant="dark">
                <Navbar.Brand href="/">MetadataWiper</Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav>
                        <Nav.Link href="/pdf">PDF</Nav.Link>
                        <Nav.Link href="/xlsx">XLSX</Nav.Link>
                        <Nav.Link href="/jpeg">JPG</Nav.Link>
                        <Nav.Link href="/docx">DOCX</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
  <BrowserRouter>
    <Routes>

        <Route path="/jpeg" element={<JPEG />} />
        <Route path="/pdf" element={<PDF />} />
        <Route path="/xlsx" element={<Excel />} />
        <Route path="/docx" element={<DOCX />} />

        <Route path="/" element={<Home />} />
     </ Routes>

  </BrowserRouter>
  </div>

  )
}

export default App

