import { useState, useEffect } from 'react'
import { apiClient } from '../services/api'

export default function HelloComponent() {
  const [message, setMessage] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    fetchHello()
  }, [])

  const fetchHello = async () => {
    try {
      setLoading(true)
      setError('')
      const response = await apiClient.getHello()
      setMessage(response)
    } catch (err) {
      setError('Failed to fetch message from server')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div className="bg-white p-8 rounded-lg shadow-lg">
        <h2 className="text-2xl font-bold mb-4 text-gray-800">Server Message</h2>
        {loading && <p className="text-gray-600">Loading...</p>}
        {error && <p className="text-red-600">{error}</p>}
        {message && (
          <div className="bg-blue-50 p-4 rounded-lg border-l-4 border-blue-600">
            <p className="text-xl text-blue-600 font-semibold">{message}</p>
          </div>
        )}
        <button
          onClick={fetchHello}
          className="mt-4 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          Refresh Message
        </button>
      </div>
      <div className="bg-white p-8 rounded-lg shadow-lg">
        <h2 className="text-2xl font-bold mb-4 text-gray-800">About</h2>
        <p className="text-gray-700 mb-4">
          This is a simple React UI for the Spring Boot Payment System. The backend is running and handles payment processing with multiple adapters.
        </p>
        <ul className="list-disc list-inside text-gray-700 space-y-2">
          <li>UPI and Card payment methods</li>
          <li>Idempotency support for safe retries</li>
          <li>Payment history and reporting</li>
          <li>Secure authentication</li>
        </ul>
      </div>
    </div>
  )
}
