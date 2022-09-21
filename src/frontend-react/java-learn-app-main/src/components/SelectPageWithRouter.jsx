import React from "react";
import TabPanel from "./TabPanel";
import CitiesTable from "./CitiesTable";
import { AppBar, Button, Tab, Tabs, TextField, Typography, } from "@material-ui/core";
import {Link, Switch, Route } from "react-router-dom";
import { withRouter } from "react-router";
import CityService from '../services/CityService';

function a11yProps(index) {
  return {
    id: `full-width-tab-${index}`,
    "aria-controls": `full-width-tabpanel-${index}`,
  };
}
class SelectPage extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      prop: 42,
      tabValue: 0,
      queryString: 'SELECT * FROM city;',
      allCities: [],
      filteredCities: [],
      deleteCityErrors: {},
      selectFromCitiesErrors: {}
    };

    this.handleQueryChange = this.handleQueryChange.bind(this);
    this.handleSubmitQuery = this.handleSubmitQuery.bind(this);
  }

  componentDidMount() {
    const queryString = this.state.queryString;

    CityService.selectFromCities(queryString).then((res) => {
      this.setState({ allCities: res.data })
    });
  }

  handleQueryChange = (event) => {
    this.setState({
        queryString: event.target.value,
    });
  };

  handleSubmitQuery = () => {
    const queryString = this.state.queryString;

    if (queryString.toLowerCase().includes('DELETE'.toLowerCase())) {
        CityService.deleteCity(queryString).then(res => {
            this.setState({ allCities: res.data });
            this.setState({ deleteCityErrors: null });
        })
        .catch(err => {
        this.setState({ deleteCityErrors: err.response.data });
        this.setState({ selectFromCitiesErrors: null });
        });
    };

    CityService.selectFromCitiesByParameters(queryString).then(res => {
        this.setState({ filteredCities: res.data });
        this.setState({ selectFromCitiesErrors: null });
    })
    .catch(err => {
        this.setState({ selectFromCitiesErrors: err.response.data });
        this.setState({ deleteCityErrors: null });
    });
  }

  render() {
    const { allCities, filteredCities, tabValue, deleteCityErrors, selectFromCitiesErrors } = this.state;
    const { path } = this.props.match;

    return (
      <>
        <Switch>
          <Route exact path={path}>
            <div className="buttons-container">
                <Button component={Link} to="/" variant="contained" color="secondary">
                    Back To Create Query
                </Button>
                <Button component={Link} to="/cityInfo" variant="contained" color="secondary">
                    City info, insert and update query
                </Button>
             </div>
              <div className="city-creation-form-container__title">
                    <Typography display="block" variant="h3">
                        Select and delete query
                    </Typography>
              </div>
              <div className="has-error" align="center">
                {
                    deleteCityErrors !== null ?
                        <div>
                            { deleteCityErrors.info }
                        </div>
                    :
                        ""
                }
                {
                    selectFromCitiesErrors !== null ?
                        <div>
                            { selectFromCitiesErrors.info }
                        </div>
                    :
                        ""
                }
              </div>
              <div className="city-data-container__enter-comment-section enter-comment-section">
                <TextField
                    label="Select Query"
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
               <div className="table-container">
               <AppBar position="static">
                <Tabs
                  variant="fullWidth"
                  onChange={this.handleTabChange}
                  value={tabValue}
                >
                    <Tab label="All cities" {...a11yProps(0)} />
                </Tabs>
                <TabPanel value={tabValue} index={0}>
                  <CitiesTable
                    cities={
                         filteredCities.length ? filteredCities : allCities
                    }
                  />
                </TabPanel>
              </AppBar>
            </div>
          </Route>
        </Switch>
      </>
    );
  }
}

const SelectPageWithRouter = withRouter(SelectPage);
export default SelectPageWithRouter;