import Link from 'next/link'

const workerNavbar = () => {
  return (
    <nav className="bg-gray-800 p-4">
      <div className="container mx-auto flex justify-between items-center">
        <div className="text-white text-lg font-bold">ABBA Worker Console</div>
        <div className="flex space-x-4">
          <Link href="/staff/JobsDisplay" className="text-white hover:text-gray-400">Jobs Display</Link>
          <Link href="/staff/LeaveApplication" className="text-white hover:text-gray-400">Leave Application</Link>
        </div>
      </div>
    </nav>
  )
}

export default workerNavbar