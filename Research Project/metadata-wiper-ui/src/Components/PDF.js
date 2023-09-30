import React from "react"
import axios from 'axios';
import download from 'downloadjs'

export default class PDF extends React.Component {
    state = {
        pdf: null
      };
    
      handleChange = (e) => {
        this.setState({
          [e.target.id]: e.target.value
        })
      };
    
      handleFileChange = (e) => {
        this.setState({
          pdf: e.target.files[0]
        })
      };
    
      handleSubmit = (e) => {
        e.preventDefault();
        console.log(this.state);
        let form_data = new FormData();
        form_data.append('pdf_file', this.state.pdf);

        let url = 'http://localhost:8000/api/pdf/';
        axios.post(url, form_data, {
          headers: {
            'content-type': 'multipart/form-data'
          },
          responseType: 'blob'
        }).then(function(resp) {
          return download(resp.data, "file.pdf");
        }).catch(err => console.log("Error: " + err))
      };
    
      // need to fix accepted file types on input
      render() {
        return (
          <div className="PDF">
            <h1>PDF Metadata Wiper</h1>
            <form onSubmit={this.handleSubmit}>
              <p>
            
                <input type="file"
                       id="pdf"
                       accept="application/pdf"  onChange={this.handleFileChange} required/>
              </p>
              <input type="submit"/>
            </form>
          </div>
        );
      }


}
