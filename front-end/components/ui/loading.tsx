import React from 'react'

const Loading = () => {
  return (
    <div className="fixed inset-0 flex items-center justify-center bg-white z-50">
      <div className="text-center">
        <div className="text-lg font-bold mb-4">Loading...</div>
        <div className="w-64 h-2 bg-gray-200 rounded-full overflow-hidden">
          <div className="h-full bg-blue-500 animate-progress"></div>
        </div>
      </div>
      <style jsx>{`
        .animate-progress {
          animation: progress 2s infinite;
        }

        @keyframes progress {
          0% { width: 0%; }
          100% { width: 100%; }
        }
      `}</style>
    </div>
  )
}

export default Loading