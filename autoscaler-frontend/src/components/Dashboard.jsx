import React, { useEffect, useState } from "react";
import axios from "axios";

const Dashboard = () => {
  const [cpuUsage, setCpuUsage] = useState(0.0);
  const [currentReplicas, setCurrentReplicas] = useState(1);
  const [error, setError] = useState("");

  // Fetch CPU Usage from backend
  const fetchCpuUsage = async () => {
    try {
      const res = await axios.get("http://localhost:8090/api/cpu-usage"); // proxy handles this
      const usage = parseFloat(res.data);
      setCpuUsage(usage);
    } catch (err) {
      console.error("Error fetching CPU usage:", err);
      setError("Unable to fetch CPU usage");
    }
  };

  // Fetch number of replicas from backend
  const fetchCurrentReplicas = async () => {
    try {
      const res = await axios.get("http://localhost:8090/api/replicas"); // proxy handles this
      const replicas = parseInt(res.data);
      setCurrentReplicas(replicas);
    } catch (err) {
      console.error("Error fetching replica count:", err);
      setError("Unable to fetch replica count");
    }
  };
  const trigger=async()=>{
    try{

      const res=await axios.post("http://localhost:8090/api/evaluate-scaling")
    } catch(error){
      console.error("not possible to scale");
    }

  }
  // Periodically fetch metrics
  useEffect(() => {
    fetchCpuUsage();
    fetchCurrentReplicas();

    const interval = setInterval(() => {
      fetchCpuUsage();
      fetchCurrentReplicas();
      trigger();
    }, 5000); // every 30 seconds

    return () => clearInterval(interval);
  }, []);

  return (
    <div className="bg-white rounded-xl p-6 shadow-md max-w-xl mx-auto mt-10">
      <h1 className="text-3xl font-semibold mb-4 text-center text-blue-700">
        Docker Auto-Scaler Dashboard
      </h1>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      <div className="mb-6">
        <h2 className="text-xl font-bold mb-2">Real-time CPU Metrics</h2>
        <p className="text-gray-700 text-lg">
          CPU Usage:{" "}
          <span className="font-semibold text-blue-600">
          {!isNaN(cpuUsage) ? cpuUsage.toFixed(2) + "%" : "Loading..."}

          </span>
        </p>
      </div>

      <div>
        <h2 className="text-xl font-bold mb-2">Auto Scaling Status</h2>
        <p className="text-gray-700 text-lg">
          Current Replicas (Auto-Scaled):{" "}
          <span className="font-semibold text-green-600">
          {!isNaN(currentReplicas) ? currentReplicas : "Loading..."}

          </span>
        </p>
      </div>
    </div>
  );
};

export default Dashboard;
