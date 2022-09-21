import "./App.css";

import SelectPageWithRouter from "./components/SelectPageWithRouter";
import CityCreationPageWithRouter from "./components/CityCreationPage";
import CityInfoPage from "./components/CityInfoPage";

import {
  BrowserRouter as Router,
  Route,
  Switch,
} from "react-router-dom";

function App() {
  return (
    <Router>
      <Switch>
        <Route path="/cities" exact component={SelectPageWithRouter} />
        <Route path="/cityInfo" component={CityInfoPage} />
        <Route path="/">
          <CityCreationPageWithRouter />
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
