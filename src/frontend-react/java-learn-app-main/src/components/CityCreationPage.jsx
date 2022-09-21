import React from "react";
import {
  Button,
  FormControl,
  TextField,
  Typography,
} from "@material-ui/core";
import { Link, withRouter } from "react-router-dom";
import CityService from '../services/CityService';

class CityCreationPage extends React.Component {

  constructor(props) {
    super(props);

     this.state = {
        queryString: 'CREATE TABLE city (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), country_code VARCHAR(50), district VARCHAR(50), population BIGINT)";',
        createTableErrors: {}
     };

     this.handleQueryChange = this.handleQueryChange.bind(this);
     this.handleSubmitQuery = this.handleSubmitQuery.bind(this);
  }

  handleQueryChange = (event) => {
    this.setState({
      queryString: event.target.value,
    });
  };

  handleSubmitQuery = () => {
    const queryString = this.state.queryString;

    CityService.createTableCity(queryString).then(res => {
        this.props.history.push('/cityInfo');
    })
    .catch(err => {
        this.setState({ createTableErrors: err.response.data })
    });
  }

  render() {
    return (
      <div className="city-creation-form-container" >
        <header className="city-creation-form-container__navigation-container">
          <Button component={Link} to="/cities" variant="contained" color="secondary">
            All Cities
          </Button>
        </header>
        <div className="city-creation-form-container__title">
          <Typography display="block" variant="h3">
            Build query
          </Typography>
        </div>
        <div className="has-error" align="center">
            {
                this.state.createTableErrors !== null ?
                    <div>
                          {this.state.createTableErrors.info}
                    </div>
                :
                ''
            }
        </div>
        <div className="city-creation-form-container__form">
          <div className="inputs-section">
            <FormControl>
             <TextField
              label="Build Query"
              multiline
              rows={4}
              variant="outlined"
              value={this.state.queryString}
              className="creation-text-field creation-text-field_width680"
              onChange={this.handleQueryChange}
             />
            </FormControl>
          </div>
          <section className="submit-button-section">
            <Button
              variant="contained" onClick={this.handleSubmitQuery} color="primary">
              Submit
            </Button>
          </section>
        </div>
      </div>
    );
  }
}

const CityCreationPageWithRouter = withRouter(CityCreationPage);
export default CityCreationPageWithRouter;
