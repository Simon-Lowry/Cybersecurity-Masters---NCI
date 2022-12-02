import "bootstrap/dist/css/bootstrap.min.css"
import "./App.css"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import Login from "./components/Login"
import Signup from "./components/Signup"
import UserProfile from "./components/UserProfile"
import Quotes from "./components/Quotes"
import EditAccount from "./components/EditAccount"


function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/signUp" element={<Signup />} />
        <Route path="/userProfile" element={<UserProfile />} />
        <Route path="/quotes" element={<Quotes />} />
        <Route path="/editAccount" element={<EditAccount />} />

      </Routes>
    </BrowserRouter>
  )
}

export default App