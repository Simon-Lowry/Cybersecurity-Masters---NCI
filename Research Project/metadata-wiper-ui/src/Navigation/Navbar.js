import React from "react";
import 'bootstrap/dist/css/bootstrap.css';

const Navbar = () => {
    return (
        <nav class="navbar navbar-dark bg-primary">
            <div class="container-fluid">
                <div class="navbar-header">
                <a class="navbar-brand" href="#">MetadataWiper</a>
                </div>
                <ul class="nav navbar-nav">
                    <li class="active" color="white"><a href="#">Home</a></li>
                    <li><a href="#">Page 1</a></li>
                    <li><a href="#">Page 2</a></li>
                    <li><a href="#">Page 3</a></li>
                </ul>
            </div>
        </nav>
    )
}
export default Navbar