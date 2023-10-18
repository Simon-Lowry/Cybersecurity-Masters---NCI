import React from "react"
import axios from 'axios';
import download from 'downloadjs'
import { Card } from 'react-bootstrap';
import "bootstrap/dist/css/bootstrap.min.css";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

export default class PDF extends React.Component {
    state = {
        pdf: null,
        errorOnWiping: null,
        successMsg: null
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
        }).then((resp) => {
          
          this.setState({
             successMsg: 'Metadata successfully removed from file. Downloading modified file.'
           })
            return download(resp.data, "file.pdf");
        }).catch(
           async err => {
            console.log("Error 2 status: " + err)
            let error =  JSON.parse( await err.response.data.text());

            console.log("Error 2 message: " + error['exception'])
            
            this.setState({
              errorOnWiping:error['exception']
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
                    <h1>PDF Metadata Wiper</h1>
                    <p>Wipes the metadata of PDF files</p>
            </div>
            <br/> <br/>
          <Container fluid>
            <Row className="justify-content-md-center">
              <Col md="3">
              <Card border="primary">
                <Card.Title >Enter PDF file here:</Card.Title>
                <div className="PDF">
                  <form onSubmit={this.handleSubmit}>
                    <p>
                  
                      <input type="file"
                            id="pdf"
                            accept="application/pdf"  onChange={this.handleFileChange} required/>
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
            </Card>
            </Col>
            </Row>    
          </Container>
          </div>
        );
      }


}
