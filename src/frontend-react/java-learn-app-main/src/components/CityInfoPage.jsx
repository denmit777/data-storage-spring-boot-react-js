import React from "react";
import { Link } from "react-router-dom";
import { withRouter } from "react-router";
import CityService from '../services/CityService';

import {
  Button,
  Paper,
  Tabs,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableRow,
  Typography,
  TextField,
} from "@material-ui/core";

class CityInfoPage extends React.Component {
  constructor(props) {
    super(props)

    this.state = {
      tabValue: 0,
      city: {},
      queryString: '',
      createCityErrors: {},
      updateCityErrors: {},
      getCityByIdErrors: {}
    }

    this.handleQueryChange = this.handleQueryChange.bind(this);
    this.handleSubmitQuery = this.handleSubmitQuery.bind(this);
  }

  componentDidMount() {
    CityService.getCityById(this.state.queryString).then(res => {
      this.setState({ city: res.data });
    })
  }

  handleTabChange = (event, value) => {
    this.setState({
      tabValue: value,
    });
  };

  handleQueryChange = (event) => {
    this.setState({
        queryString: event.target.value,
    });
  };

  handleSubmitQuery = () => {
    const queryString = this.state.queryString;

   if (queryString.toLowerCase().includes('UPDATE'.toLowerCase())) {
        CityService.updateCity(queryString).then(res => {
            this.setState({ city: res.data });
            this.setState({ updateCityErrors: null })
        })
        .catch(err => {
            this.setState({ updateCityErrors: err.response.data });
            this.setState({ createCityErrors: null });
            this.setState({ getCityByIdErrors: null })
        });
    }

    if (queryString.toLowerCase().includes('SELECT'.toLowerCase())) {
        CityService.getCityById(this.state.queryString).then(res => {
            this.setState({ city: res.data });
            this.setState({ getCityByIdErrors: null })
        })
        .catch(err => {
            this.setState({ getCityByIdErrors: err.response.data });
            this.setState({ createCityErrors: null });
            this.setState({ updateCityErrors: null })
        });
     }

     else {
         CityService.insertCity(queryString).then(res => {
            this.setState({ city: res.data });
            this.setState({ createCityErrors: null })
         })
         .catch(err => {
            this.setState({ createCityErrors: err.response.data });
            this.setState({ updateCityErrors: null });
            this.setState({ getCityByIdErrors: null })
         });
     }
  }

  render() {
    return (
          <div className="city-data-container">
            <div className={"buttons-container"}>
              <Button component={Link} to="/cities" variant="contained" color="secondary">
                All Cities
              </Button>
              <Button component={Link} to="/" variant="contained" color="secondary">
                Back To Create Query
            </Button>
            </div>
            <div className="city-creation-form-container__title">
                <Typography display="block" variant="h3">
                    Query for insert, update and get City
                </Typography>
            </div>
            <div className="has-error" align="center">
                {
                    this.state.createCityErrors !== null ?
                        <div>
                            { this.state.createCityErrors.info }
                        </div>
                    :
                        ""
                }
                {
                    this.state.updateCityErrors !== null ?
                        <div>
                            { this.state.updateCityErrors.info }
                        </div>
                    :
                        ""
                }
                {
                    this.state.getCityByIdErrors !== null ?
                        <div>
                            { this.state.getCityByIdErrors.info }
                        </div>
                    :
                        ""
                }
            </div>
            <div className="city-data-container__enter-comment-section enter-comment-section">
               <TextField
                 label="Query for insert, update and get City"
                 multiline
                 rows={4}
                 variant="outlined"
                 value={this.state.queryString}
                 className="creation-text-field creation-text-field_width680"
                 onChange={this.handleQueryChange}
               />
            </div>
              <section className="submit-button-section">
                 <Button
                     variant="contained" onClick={this.handleSubmitQuery} color="primary">
                     Submit
                </Button>
              </section>
            <div className="city-data-container__title">
              <Typography variant="h4">{`City(${this.state.city.id}) - ${this.state.city.name}`}</Typography>
            </div>
            <div className="city-data-container__info">
              <TableContainer className="city-table" component={Paper}>
                <Table>
                  <TableBody>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Name:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.city.name}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Country Code:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.city.countryCode}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          District:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.city.district}
                        </Typography>
                      </TableCell>
                    </TableRow>
                    <TableRow>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          Population:
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography align="left" variant="subtitle1">
                          {this.state.city.population}
                        </Typography>
                      </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
             </div>
          </div>
    );
  }
}

const CityInfoPageWithRouter = withRouter(CityInfoPage);
export default CityInfoPageWithRouter;

