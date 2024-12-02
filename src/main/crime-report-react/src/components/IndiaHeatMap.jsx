import { useEffect, useState, useMemo } from 'react';
import * as d3 from 'd3';
import geoJson from './india-states.json'; // Import the GeoJSON file

const IndianHeatmap = () => {
  const [data, setData] = useState([]);
  const [crimes, setCrimes] = useState('');
  const [years, setYears] = useState('');
  const [allCrimes, setAllCrimes] = useState([]);
  const [allYears, setAllYears] = useState([]);
  const [hoveredState, setHoveredState] = useState('');
  const [loading, setLoading] = useState(false); // Track loading state
  const [error, setError] = useState(''); // Track error state

  // Fetch crime types and years when component mounts
  useEffect(() => {
    fetchCrimes();
    fetchYears();
  }, []);

  // Helper function to merge state data for "D & N Haveli" and "Daman & Diu"
  const mergeStates = (data) => {
    return data.reduce((acc, item) => {
      if (['D & N Haveli', 'Daman & Diu'].includes(item.state.stateName)) {
        const existing = acc.find(i => i.state.stateName === 'Dādra and Nagar Haveli and Damān and Diu');
        if (existing) {
          existing.count += item.count;
        } else {
          acc.push({
            ...item,
            state: { ...item.state, stateName: 'Dādra and Nagar Haveli and Damān and Diu' },
          });
        }
      } else {
        acc.push(item);
      }
      return acc;
    }, []);
  };

  // Fetch crime types
  const fetchCrimes = async () => {
    setLoading(true);
    try {
      const response = await fetch('/api/v1/crimes');
      const result = await response.json();
      setAllCrimes(result);
      if (result.length > 0) {
        setCrimes(result[0].crimeId); // Default to the first crime
      }
    } catch (error) {
      console.error('Error fetching crimes:', error);
      setError('Failed to load crime data');
    } finally {
      setLoading(false);
    }
  };

  // Fetch years
  const fetchYears = async () => {
    setLoading(true);
    try {
      const response = await fetch('/api/v1/years');
      const result = await response.json();
      setAllYears(result);
      if (result.length > 0) {
        setYears(result[0].yearId); // Default to the first year
      }
    } catch (error) {
      console.error('Error fetching years:', error);
      setError('Failed to load year data');
    } finally {
      setLoading(false);
    }
  };

  // Fetch data based on crime and year
  const fetchData = async () => {
    setLoading(true);
    setError('');
    try {
      const params = {};
      if (crimes) params.crimes = crimes;
      if (years) params.years = years;

      const response = await fetch(`/api/v1/reports?${new URLSearchParams(params)}`);
      const result = await response.json();

      const adjustedData = mergeStates(result);
      setData(adjustedData);
    } catch (error) {
      console.error('Error fetching data:', error);
      setError('Failed to load crime data for selected filters');
    } finally {
      setLoading(false);
    }
  };

  // Map rendering logic with D3.js
  const renderMap = () => {
    const svg = d3.select('#map');
    svg.selectAll('*').remove(); // Clear existing map before re-rendering

    const projection = d3.geoMercator().fitSize([svg.node().clientWidth, svg.node().clientHeight], geoJson);
    const path = d3.geoPath().projection(projection);

    svg
      .selectAll('path')
      .data(geoJson.features)
      .join('path')
      .attr('d', path)
      .attr('fill', (d) => {
        const report = data.find((item) => item.state.stateName === d.properties.name);
        const count = report ? report.count : 0;
        return colorScale(count); // Use the memoized colorScale here
      })
      .attr('stroke', 'black')
      .attr('stroke-width', 0.5)
      .on('mouseover', function (event, d) {
        d3.select(this).attr('fill', 'orange'); // Highlight on hover
        const report = data.find((item) => item.state.stateName === d.properties.name);
        const stateData = report ? `${d.properties.name}: ${report.count} Crimes` : `${d.properties.name}: No Data`;
        setHoveredState(stateData);
      })
      .on('mouseout', function (event, d) {
        const report = data.find((item) => item.state.stateName === d.properties.name);
        const count = report ? report.count : 0;
        d3.select(this).attr('fill', colorScale(count)); // Reset color
        setHoveredState('');
      });
  };

  // Re-render map when crime or year changes
  useEffect(() => {
    if (crimes && years) {
      fetchData(); // Automatically fetch data when filters change
    }
  }, [crimes, years]);

  useEffect(() => {
    if (data.length > 0) {
      renderMap(); // Re-render map after data is fetched
    }
  }, [data]);

  // Memoize color scale for performance optimization
  const colorScale = useMemo(() => {
    const crimeCounts = data.map((item) => item.count);
    const minCrime = Math.min(...crimeCounts);
    const maxCrime = Math.max(...crimeCounts);
    return d3.scaleLinear().domain([minCrime, maxCrime]).range(['lightblue', 'darkblue']);
  }, [data]);

  return (
    <div style={styles.container}>
      <div style={styles.leftColumn}>
        <div style={styles.headingAndFilters}>
          <h1>Crimes Against Women in India (2001-2021)</h1>
          <div style={styles.filters}>
            <div style={styles.filterContainer}>
              <label style={styles.label}>Crimes:</label>
              <select value={crimes} onChange={(e) => setCrimes(e.target.value)} style={styles.select}>
                {allCrimes.map((crime) => (
                  <option key={crime.crimeId} value={crime.crimeId}>
                    {crime.crimeName}
                  </option>
                ))}
              </select>
            </div>
            <div style={styles.filterContainer}>
              <label style={styles.label}>Years:</label>
              <select value={years} onChange={(e) => setYears(e.target.value)} style={styles.select}>
                {allYears.map((year) => (
                  <option key={year.yearId} value={year.yearId}>
                    {year.yearName}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {/* Error and Loading States */}
          {loading && <p>Loading data...</p>}
          {error && <p style={{ color: 'red' }}>{error}</p>}

          {/* Hovered state data display */}
          {hoveredState && (
            <div style={styles.hoveredStateData}>
              <p>{hoveredState}</p>
            </div>
          )}
        </div>
      </div>

      <div style={styles.rightColumn}>
        <div style={styles.mapContainer}>
          <svg id="map" style={styles.map} aria-label="Map showing crime data in India"></svg>
        </div>
      </div>
    </div>
  );
};

const styles = {
  container: {
    display: 'grid',
    gridTemplateColumns: 'repeat(12, 1fr)',
    gap: '20px',
    backgroundColor: '#f4f4f9',
    minHeight: '100vh',
    width: '100vw',
  },
  leftColumn: {
    gridColumn: 'span 4',
    display: 'flex',
    paddingLeft: '20px',
    flexDirection: 'column',
    justifyContent: 'flex-start',
  },
  headingAndFilters: {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'flex-start',
  },
  rightColumn: {
    gridColumn: 'span 8',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  },
  filters: {
    marginBottom: '20px',
    display: 'flex',
    flexDirection: 'column',
  },
  filterContainer: {
    marginBottom: '10px',
    display: 'flex',
    alignItems: 'center',
  },
  label: {
    fontSize: '16px',
    marginRight: '10px',
  },
  select: {
    padding: '8px',
    fontSize: '14px',
    width: '150px',
    border: '1px solid #ccc',
    borderRadius: '4px',
  },
  mapContainer: {
    width: '95vh',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '95vh',
  },
  map: {
    width: '100%',
    height: '100%',
    border: '2px solid #ccc',
  },
  hoveredStateData: {
    marginTop: '20px',
    padding: '10px',
    background: '#fff',
    borderRadius: '5px',
    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
    width: '80%',
    textAlign: 'center',
  },
};

export default IndianHeatmap;
