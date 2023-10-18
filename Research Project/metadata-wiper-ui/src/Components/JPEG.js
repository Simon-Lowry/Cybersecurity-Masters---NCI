import React from "react"
import axios from 'axios';
import FileSaver from 'file-saver';
import download from 'downloadjs'
import { Card } from 'react-bootstrap';
import "bootstrap/dist/css/bootstrap.min.css";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

export default class JPEG extends React.Component {
    state = {
        image: null,
        errorOnWiping: null,
        successMsg: null
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
  
        }).then((resp) => {
          
          this.setState({
             successMsg: 'Metadata successfully removed from file. Downloading modified file.'
           })
          return download(resp.data, "image.jpg");
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
      render() {
        let error = this.state.errorOnWiping;
        let successMsg = this.state.successMsg;

        return (
          <div>
            <div class="mt-4 p-5 bg-primary text-white rounded">
                    <h1>JPG Metadata Wiper</h1>
                    <p>Wipes the metadata of JPG files</p>
            </div>
            <br/> <br/>
          <Container fluid>

            <Row className="justify-content-md-center">
              <Col md="3">
              <Card border="primary">
              <Card.Title >Enter JPG file here:</Card.Title>
                <div className="JPEG">
                  <form onSubmit={this.handleSubmit}>
                    <p>
                      <input type="file"
                            id="image"
                            accept="image/png, image/jpeg"  onChange={this.handleImageChange} required/>
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


