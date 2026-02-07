import { useState } from 'react'
import { apiClient } from '../services/api'

interface PaymentFormProps {
  username: string
  password: string
}

export default function PaymentForm({ username, password }: PaymentFormProps) {
  const [formData, setFormData] = useState({
    amount: '',
    toAccount: '',
    fromAccount: '',
    name: '',
    method: 'upi',
  })
  const [idempotencyKey, setIdempotencyKey] = useState(() => generateIdempotencyKey())
  const [loading, setLoading] = useState(false)
  const [success, setSuccess] = useState(false)
  const [error, setError] = useState('')
  const [response, setResponse] = useState<any>(null)

  function generateIdempotencyKey() {
    return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))
  }

  const handleGenerateKey = () => {
    setIdempotencyKey(generateIdempotencyKey())
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!formData.amount || !formData.toAccount || !formData.fromAccount || !formData.name) {
      setError('All fields are required')
      return
    }

    try {
      setLoading(true)
      setError('')
      setSuccess(false)
      setResponse(null)

      const result = await apiClient.createPayment(
        parseFloat(formData.amount),
        formData.toAccount,
        formData.fromAccount,
        formData.name,
        formData.method,
        username,
        password,
        idempotencyKey
      )

      setResponse(result)
      setSuccess(true)
      
      // Reset form
      setFormData({
        amount: '',
        toAccount: '',
        fromAccount: '',
        name: '',
        method: 'upi',
      })
      setIdempotencyKey(generateIdempotencyKey())
    } catch (err: any) {
      setError(err.message || 'Failed to create payment')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <div className="bg-white p-8 rounded-lg shadow-lg">
        <h2 className="text-2xl font-bold mb-6 text-gray-800">Create Payment</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-gray-700 font-semibold mb-2">Amount</label>
            <input
              type="number"
              name="amount"
              step="0.01"
              min="0"
              value={formData.amount}
              onChange={handleChange}
              placeholder="100.00"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-600"
              required
            />
          </div>

          <div>
            <label className="block text-gray-700 font-semibold mb-2">From Account</label>
            <input
              type="text"
              name="fromAccount"
              value={formData.fromAccount}
              onChange={handleChange}
              placeholder="1234567890123456"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-600"
              required
            />
          </div>

          <div>
            <label className="block text-gray-700 font-semibold mb-2">To Account</label>
            <input
              type="text"
              name="toAccount"
              value={formData.toAccount}
              onChange={handleChange}
              placeholder="user@upi"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-600"
              required
            />
          </div>

          <div>
            <label className="block text-gray-700 font-semibold mb-2">Recipient Name</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="John Doe"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-600"
              required
            />
          </div>

          <div>
            <label className="block text-gray-700 font-semibold mb-2">Payment Method</label>
            <select
              name="method"
              value={formData.method}
              onChange={handleChange}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-600"
            >
              <option value="upi">UPI</option>
              <option value="card">Card</option>
            </select>
          </div>

          <div>
            <label className="block text-gray-700 font-semibold mb-2">Idempotency Key</label>
            <div className="flex gap-2">
              <input
                type="text"
                value={idempotencyKey}
                readOnly
                className="flex-1 px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 text-gray-600"
              />
              <button
                type="button"
                onClick={handleGenerateKey}
                className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600"
              >
                Generate
              </button>
            </div>
            <p className="text-xs text-gray-500 mt-1">Used for idempotency - ensures safe retries</p>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-300 text-red-700 px-4 py-3 rounded-lg">
              {error}
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed font-bold"
          >
            {loading ? 'Processing...' : 'Create Payment'}
          </button>
        </form>
      </div>

      {success && response && (
        <div className="bg-white p-8 rounded-lg shadow-lg">
          <h3 className="text-2xl font-bold mb-4 text-green-600">Success!</h3>
          <div className="space-y-3 bg-green-50 p-4 rounded-lg border border-green-300">
            <p><strong>Amount:</strong> ${response.amount}</p>
            <p><strong>From:</strong> {response.fromAccount}</p>
            <p><strong>To:</strong> {response.toAccount}</p>
            <p><strong>Recipient:</strong> {response.name}</p>
            <p><strong>Method:</strong> {response.method?.toUpperCase()}</p>
            <p className="text-sm text-gray-600 mt-4">Payment processed successfully!</p>
          </div>
        </div>
      )}

      {!success && !response && (
        <div className="bg-white p-8 rounded-lg shadow-lg">
          <h3 className="text-2xl font-bold mb-4 text-gray-800">Payment Details</h3>
          <p className="text-gray-700 mb-4">
            Fill in the form to create a new payment. The system supports both UPI and Card payments with automatic method detection.
          </p>
          <div className="bg-blue-50 p-4 rounded-lg border-l-4 border-blue-600">
            <h4 className="font-bold text-blue-900 mb-2">UPI Example:</h4>
            <ul className="text-sm text-blue-800 space-y-1">
              <li>To Account: user@upi</li>
              <li>From Account: your_bank_account</li>
            </ul>
          </div>
          <div className="bg-green-50 p-4 rounded-lg border-l-4 border-green-600 mt-4">
            <h4 className="font-bold text-green-900 mb-2">Card Example:</h4>
            <ul className="text-sm text-green-800 space-y-1">
              <li>To Account: 1234567890123456</li>
              <li>From Account: your_card_number</li>
            </ul>
          </div>
        </div>
      )}
    </div>
  )
}
