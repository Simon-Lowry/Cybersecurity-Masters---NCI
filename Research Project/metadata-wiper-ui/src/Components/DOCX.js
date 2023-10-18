import React from "react"
import axios from 'axios';
import download from 'downloadjs'
import { Card } from 'react-bootstrap';
import "bootstrap/dist/css/bootstrap.min.css";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

export default class DOCX extends React.Component {
    state = {
        docx_file: null,
        errorOnWiping: null,
        successMsg: null
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
        }).then((resp) => {
          
          this.setState({
             successMsg: 'Metadata successfully removed from file. Downloading modified file.'
           })
          return download(resp.data, "file.docx");
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
                  <h1>DOCX Metadata Wiper</h1>
                  <p>Wipes the metadata of DOCX files</p>
            </div>
            <br/> <br/>
          <Container fluid>

            <Row className="justify-content-md-center">
              <Col md="3">
              <Card border="primary">
                <div className="DOCX">
                  <Card.Title >Enter DOCX file here:</Card.Title>
                  <form onSubmit={this.handleSubmit}>
                    <p>
                  
                      <input type="file"
                            id="docx"
                            accept=".docx"  onChange={this.handleDocxChange} required/>
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
