import React from 'react';
import MainMenu from "./components/MainMenu";
import Router from "./pages/Router";
import MenuNavbar from "./components/MenuNavbar";
function App() {
    /**
     * hlavni struktura aplikace
     * komponenta <Router/> bude nahrazovana jednotlivymi komponenta podle aktualni cesty v url
     */
  return (
      <div className="App">
        <h1 className={"title center-title"}>Hogo fogo vyhledávací systém</h1>
          <MainMenu menuTitle={"Navigační menu"}/>
          <MenuNavbar/>
          <Router/>
      </div>
  );
}

export default App;
