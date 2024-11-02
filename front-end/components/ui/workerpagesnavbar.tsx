import { Link } from 'react-router-dom'

export default function workerNavbar() {
  return (
    <nav className="bg-gray-800 p-4">
      <div className="container mx-auto flex justify-between items-center">
        <div className="text-white text-lg font-bold">Worker Dashboard</div>
        <div className="flex space-x-4">
          <Link to="/jobs-display" className="text-white hover:text-gray-400">Jobs Display</Link>
          <Link to="/leave-application" className="text-white hover:text-gray-400">View Workers</Link>
        </div>
      </div>
    </nav>
  )
}