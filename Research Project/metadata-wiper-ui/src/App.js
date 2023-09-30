import React, { Component } from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom"
import JPEG from "./Components/JPEG"
import PDF from "./Components/PDF"
import Excel from "./Components/Excel"
import DOCX from "./Components/DOCX"


function App() {
  return (
  <BrowserRouter>
    <Routes>

        <Route path="/jpeg" element={<JPEG />} />
        <Route path="/pdf" element={<PDF />} />
        <Route path="/xlsx" element={<Excel />} />
        <Route path="/docx" element={<DOCX />} />

        <Route path="/" element={<JPEG />} />
     </ Routes>

  </BrowserRouter>
  )
}

export default App

