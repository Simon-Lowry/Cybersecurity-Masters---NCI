import React from "react"
import axios from 'axios';
import download from 'downloadjs'
import { Card } from 'react-bootstrap';
import "bootstrap/dist/css/bootstrap.min.css";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

export default class Excel extends React.Component {
    state = {
        xlsx_file: null,
        errorOnWiping: null,
        successMsg: null
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
        }).then((resp) => {
          
          this.setState({
             successMsg: 'Metadata successfully removed from file. Downloading modified free file.'
           })
          return download(resp.data, "file.xlsx");
        }).catch(
          async err => {
            console.log("Error 2 status: " + err)
            let error =  JSON.parse( await err.response.data.text());
            console.log("Error 2 message: " + error['error'])
            
            this.setState({
              errorOnWiping:error['error']
            })
         })
      };
    
      // need to fix accepted file types on input
      render() {
        let error = this.state.errorOnWiping;
        let successMsg = this.state.successMsg;

        return (

          <div>
          <div class="mt-4 p-5 bg-primary text-white rounded">
                <h1>XLSX Metadata Wiper</h1>
                <p>Wipes the metadata of XLSX files</p>
          </div>

          <br/> <br/>
          <Container fluid>

            <Row className="justify-content-md-center">
              <Col md="3">
               <div class="panel-group">
                <Card border="primary" >
                  <Card.Title >Enter XLSX file here:</Card.Title>
                  <Card.Text>
                <div className="XLSX">
                  <form onSubmit={this.handleSubmit}>
                    <p>
                  
                      <input type="file"
                            id="file"
                            accept=".xlsx"  onChange={this.handleXLSXChange} required/>
                    </p>
                    <input type="submit"/>
                    {error &&(
                      <div>
                        <p style={{ color: "red" }}>
                            {error}
                        </p>
                      </div>
                    )}
                    {successMsg &&(
                      <div>
                        <p style={{ color: "green" }}>
                            {successMsg}
                        </p>
                      </div>
                    )}
                  </form>
                </div>
                </Card.Text>
            
              </Card>
            </div>
            </Col>
            </Row>    
          </Container>
          </div>


        );
      }
}
