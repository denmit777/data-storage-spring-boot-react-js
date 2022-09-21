import React from "react";
import PropTypes from "prop-types";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@material-ui/core";
import { CITIES_TABLE_COLUMNS } from "../constants/tablesColumns";

function CitiesTable(props) {
  const {cities} = props;

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            {CITIES_TABLE_COLUMNS.map((item) => {
              return (
                <TableCell key={item.id} align="center">
                  <Typography variant="h6">{item.label}</Typography>
                </TableCell>
              );
            })}
          </TableRow>
        </TableHead>
        <TableBody>
          {cities.map((item, index) => {
            return (
              <TableRow key={index}>
                <TableCell align="center">{item.id}</TableCell>
                <TableCell align="center">{item.name}</TableCell>
                <TableCell align="center">{item.countryCode}</TableCell>
                <TableCell align="center">{item.district}</TableCell>
                <TableCell align="left">{item.population}</TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

CitiesTable.propTypes = {
  name: PropTypes.array,
};

export default CitiesTable;
