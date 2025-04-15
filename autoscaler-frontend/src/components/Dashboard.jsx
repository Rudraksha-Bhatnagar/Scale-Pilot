import React, { useEffect, useState } from "react";
import axios from "axios";

const Dashboard = () => {
  const [cpuUsage, setCpuUsage] = useState(0.0);
  const [manualReplicas, setManualReplicas] = useState(1);

  const fetchCpuUsage = async () => {
    try {
      const res = await axios.get("http://localhost:8090/api/cpu-usage");
      setCpuUsage(res.data);
    } catch (err) {
      console.error("Failed to fetch CPU usage", err);
    }
  };

  const fetchManualReplicas = async () => {
    try {
      const res = await axios.get("http://localhost:8090/api/manual-replicas");
      setManualReplicas(res.data);
    } catch (err) {
      console.error("Failed to fetch replica count", err);
    }
  };

  const scaleManually = async (replicas) => {
    try {
      await axios.post(`http://localhost:8090/api/manual-scale?replicas=${replicas}`);
      alert(`Scaling triggered to ${replicas} replicas.`);
      setManualReplicas(replicas);
    } catch (err) {
      console.error("Failed to scale manually", err);
    }
  };

  useEffect(() => {
    fetchCpuUsage();
    fetchManualReplicas();
    const interval = setInterval(() => {
      fetchCpuUsage();
      fetchManualReplicas();
    }, 5000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="bg-white rounded-xl p-6 shadow-md max-w-xl mx-auto mt-10">
      <div className="mb-6">
        <h2 className="text-2xl font-bold mb-2">Real-time CPU Metrics</h2>
        <p className="text-gray-700 text-lg">
          CPU Usage: <strong>{cpuUsage.toFixed(2)}%</strong>
        </p>
      </div>

      <div>
        <h2 className="text-2xl font-bold mb-2">Manual Scaling</h2>
        <p className="text-gray-700 text-lg mb-4">
          Current Replicas: <strong>{manualReplicas}</strong>
        </p>
        <div className="flex space-x-4">
          <button
            onClick={() => scaleManually(1)}
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Scale to 1
          </button>
          <button
            onClick={() => scaleManually(3)}
            className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
          >
            Scale to 3
          </button>
          <button
            onClick={() => scaleManually(5)}
            className="bg-purple-500 text-white px-4 py-2 rounded hover:bg-purple-600"
          >
            Scale to 5
          </button>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
