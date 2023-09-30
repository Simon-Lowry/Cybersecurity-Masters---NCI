import React from "react"
import axios from 'axios';
import download from 'downloadjs'

export default class Excel extends React.Component {
    state = {
        xlsx_file: null
      };
    
      handleChange = (e) => {
        this.setState({
          [e.target.id]: e.target.value
        })
      };
    
      handleXLSXChange = (e) => {
        this.setState({
            xlsx_file : e.target.files[0]
        })
      };
    
      handleSubmit = (e) => {
        e.preventDefault();
        console.log(this.state);
        let form_data = new FormData();
        form_data.append('xlsx_file', this.state.xlsx_file, this.state.xlsx_file.name);

        let url = 'http://localhost:8000/api/xlsx/';
        axios.post(url, form_data, {
          headers: {
            'content-type': 'multipart/form-data'
          },
          responseType: 'blob'
        }).then(function(resp) {
          console.log( resp)
          return download(resp.data, "file.xlsx");
        }).catch(err => console.log("Error: " + err))
      };
    
      // need to fix accepted file types on input
      render() {
        return (
          <div className="XLSX">
            <h1>XLSX Metadata Wiper</h1>
            <form onSubmit={this.handleSubmit}>
              <p>
            
                <input type="file"
                       id="file"
                       accept=".xlsx"  onChange={this.handleXLSXChange} required/>
              </p>
              <input type="submit"/>
            </form>
          </div>
        );
      }
}
