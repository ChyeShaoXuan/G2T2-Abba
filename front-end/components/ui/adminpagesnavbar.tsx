import { Link } from 'react-router-dom'

export default function Navbar() {
  return (
    <nav className="bg-gray-800 p-4">
      <div className="container mx-auto flex justify-between items-center">
        <div className="text-white text-lg font-bold">Admin Dashboard</div>
        <div className="flex space-x-4">
          <Link to="/view-clients" className="text-white hover:text-gray-400">View Clients</Link>
          <Link to="/view-workers" className="text-white hover:text-gray-400">View Workers</Link>
          <Link to="/overwrite-tasks" className="text-white hover:text-gray-400">Overwrite Tasks</Link>
          <Link to="/job-statistics" className="text-white hover:text-gray-400">Job Statistics</Link>
        </div>
      </div>
    </nav>
  )
}