import React from "react"
import axios from 'axios';
import download from 'downloadjs'

export default class DOCX extends React.Component {
    state = {
        docx_file: null
      };
    
      handleChange = (e) => {
        this.setState({
          [e.target.id]: e.target.value
        })
      };
    
      handleDocxChange = (e) => {
        this.setState({
            docx_file: e.target.files[0]
        })
      };
    
      handleSubmit = (e) => {
        e.preventDefault();
        console.log(this.state);
        let form_data = new FormData();
        form_data.append('docx_file', this.state.docx_file, this.state.docx_file.name);

        let url = 'http://localhost:8000/api/docx/';
        axios.post(url, form_data, {
          headers: {
            'content-type': 'multipart/form-data'
          },
          responseType: 'blob'
        }).then(function(resp) {
          console.log( resp)
          return download(resp.data, "file.docx");
        }).catch(err => console.log("Error: " + err))
      };
    
      // need to fix accepted file types on input
      render() {
        return (
          <div className="DOCX">
            <h1>DOCX Metadata Wiper</h1>
            <form onSubmit={this.handleSubmit}>
              <p>
            
                <input type="file"
                       id="docx"
                       accept=".docx"  onChange={this.handleDocxChange} required/>
              </p>
              <input type="submit"/>
            </form>
          </div>
        );
      }

}
