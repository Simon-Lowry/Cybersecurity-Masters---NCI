import React from "react"
import axios from 'axios';
import FileSaver from 'file-saver';
import download from 'downloadjs'

export default class JPEG extends React.Component {
    state = {
        image: null
      };
    
      handleChange = (e) => {
        this.setState({
          [e.target.id]: e.target.value
        })
      };
    
      handleImageChange = (e) => {
        this.setState({
          image: e.target.files[0]
        })
      };
    
      handleSubmit = (e) => {
        e.preventDefault();
        console.log("File being sent for metadata wiping: " + this.state);
        let form_data = new FormData();
        form_data.append('image', this.state.image, this.state.image.name);
        let url = 'http://localhost:8000/api/jpeg/';
        axios.post(url, form_data,  {
          headers: {
            'content-type': 'multipart/form-data',
          },
          responseType: 'blob'
  
        }).then(function(resp) {
          console.log(  resp.data)
          return download(resp.data, "image.jpg");
        }).catch(err => console.log("Error: " + err))
      };
      render() {
        return (
          <div className="JPEG">
            <h1>JPEG Metadata Wiper</h1>

            <form onSubmit={this.handleSubmit}>
              <p>
                <input type="file"
                       id="image"
                       accept="image/png, image/jpeg"  onChange={this.handleImageChange} required/>
              </p>
              <input type="submit"/>
            </form>
          </div>
        );
      }


}


